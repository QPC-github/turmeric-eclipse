/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
/**
 * 
 */
package org.ebayopensource.turmeric.eclipse.resources.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ebayopensource.turmeric.eclipse.resources.Activator;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils;
import org.ebayopensource.turmeric.eclipse.utils.plugin.EclipseMessageUtils.SOAResourceStatus;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;


/**
 * @author yayu
 * 
 */
public final class MarkerUtil {
	public static final String SOA_PROBLEM_MARKER_ID = Activator.PLUGIN_ID
	+ ".soaproblem";

	public static final String WSDL_PROBLEM_MARKER_ID = Activator.PLUGIN_ID
	+ ".wsdlproblem";

	/**
	 * 
	 */
	private MarkerUtil() {
		super();
	}

	public static IMarker[] findSOAErrorMarkers(IResource resource)
	throws CoreException {
		final List<IMarker> result = new ArrayList<IMarker>();
		for (final IMarker marker : findSOAProblemMarkers(resource)) {
			if (IMarker.SEVERITY_ERROR == marker.getAttribute(IMarker.SEVERITY,
					IMarker.SEVERITY_ERROR)) {
				result.add(marker);
			}
		}
		return result.toArray(new IMarker[0]);
	}

	public static IMarker[] findSOAProblemMarkers(IResource resource)
	throws CoreException {
		return resource.findMarkers(SOA_PROBLEM_MARKER_ID, true,
				IResource.DEPTH_INFINITE);
	}

	public static IMarker[] findMarkers(IResource resource, String type)
	throws CoreException {
		return resource.findMarkers(type, true, IResource.DEPTH_INFINITE);
	}

	public static void cleanSOAProblemMarkers(IResource resource)
	throws CoreException {
		cleanMarkers(resource, SOA_PROBLEM_MARKER_ID);
	}

	public static void cleanMarkers(IResource resource, String type)
	throws CoreException {
		if (resource == null) {
			return;
		}
		final IMarker[] markers = resource.findMarkers(type, true,
				IResource.DEPTH_INFINITE);
		for (final IMarker marker : markers) {
			if (marker.exists()) {
				marker.delete();
			}
		}
	}

	private static int getMarkerSeverity(IStatus status) {
		switch (status.getSeverity()) {
		case IStatus.ERROR:
			return IMarker.SEVERITY_ERROR;
		case IStatus.WARNING:
			return IMarker.SEVERITY_WARNING;
		default:
			return IMarker.SEVERITY_INFO;
		}
	}

	public static IMarker[] createSOAProblemMarker(Throwable thrown,
			IResource resource) throws CoreException {
		return createMarker(EclipseMessageUtils.createErrorStatus(thrown),
				resource, SOA_PROBLEM_MARKER_ID);
	}

	public static IMarker[] createSOAProblemMarkerRecursive(IStatus status,
			IProject project) throws CoreException {
		ArrayList<IMarker> markersList = new ArrayList<IMarker>();
		if (status instanceof MultiStatus) {
			for (IStatus childStatus : ((MultiStatus) status).getChildren()) {
				createSOAProblemMarkerRecursive(childStatus, project);
			}
		} else if (status instanceof IResourceStatus) {
			IResource resource = project.getFile(((IResourceStatus) status)
					.getPath());
			markersList.addAll(Arrays.asList(createSOAProblemMarker(status,
					resource)));

		} else {
			markersList.addAll(Arrays.asList(createSOAProblemMarker(status,
					project)));

		}
		return markersList.toArray(new IMarker[0]);
	}

	public static IMarker[] createSOAProblemMarker(IStatus status,
			IResource resource) throws CoreException {
		return createMarker(status, resource, SOA_PROBLEM_MARKER_ID);
	}

	public static IMarker[] createMarker(IStatus status, IResource resource,
			String type) throws CoreException {
		if (status == null || resource == null || status.isOK()) {
			return new IMarker[0];
		}
		final List<IMarker> result = new ArrayList<IMarker>();
		if (status.isMultiStatus()) {
			for (final IStatus stat : status.getChildren()) {
				IMarker marker = createSingleMarker(stat, resource, type);
				if (marker != null) {
					result.add(marker);
				}
				marker = createRootCauseMarker(stat, resource, type);
				if (marker != null) {
					result.add(marker);
				}
			}
		} else {
			IMarker marker = createSingleMarker(status, resource, type);
			if (marker != null) {
				result.add(marker);
			}
			marker = createRootCauseMarker(status, resource, type);
			if (marker != null) {
				result.add(marker);
			}
		}

		return result.toArray(new IMarker[0]);
	}

	private static IMarker createRootCauseMarker(IStatus status,
			IResource resource, String type) throws CoreException {
		Throwable cause = status.getException();
		if (cause == null || cause.getCause() == null) {
			return null;
		}

		while (cause.getCause() != null) {
			cause = cause.getCause();
		}
		IStatus rootStatus = EclipseMessageUtils
		.createStatus(status.getPlugin(), cause.getLocalizedMessage(),
				status.getSeverity());
		return createSingleMarker(rootStatus, resource, type);
	}

	private static IMarker createSingleMarker(IStatus status,
			IResource resource, String type) throws CoreException {
		IMarker result = null;
		if (status.isOK() == false && status.isMultiStatus() == false) {
			if (status instanceof SOAResourceStatus
					&& ((SOAResourceStatus) status).getResource() != null
					&& ((SOAResourceStatus) status).getResource()
					.isAccessible()) {
				result = ((SOAResourceStatus) status).getResource()
				.createMarker(type);
			} else {
				result = resource.createMarker(type);
			}

			result.setAttribute(IMarker.MESSAGE, status.getMessage());
			if (status instanceof IResourceStatus
					&& ((IResourceStatus) status).getPath() != null) {
				result.setAttribute(IMarker.LOCATION,
						((IResourceStatus) status).getPath().toString());
				result.setAttribute("path", resource.getLocation().toString());
			} else {
				result.setAttribute(IMarker.LOCATION, resource.getLocation()
						.toString());
				result.setAttribute("path", resource.getLocation().toString());
			}

			result.setAttribute(IMarker.SEVERITY, getMarkerSeverity(status));
		}
		return result;
	}

	public static void createWSDLMarker(IResource wsdlFile, String prefix,
			IStatus problem, int lineNumber) throws CoreException {
		if(wsdlFile == null){
			return;
		}
		IMarker marker = createSingleMarker(problem, wsdlFile,
				WSDL_PROBLEM_MARKER_ID);
		marker.setAttribute(IMarker.MESSAGE, prefix + ":"
				+ marker.getAttribute(IMarker.MESSAGE));
		if (lineNumber > -1) {
			marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
		}
	}

	public static void cleanWSDLMarkers(IResource wsdlFile)
	throws CoreException {
		cleanMarkers(wsdlFile, WSDL_PROBLEM_MARKER_ID);
	}

}