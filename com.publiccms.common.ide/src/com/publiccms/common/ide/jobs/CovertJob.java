package com.publiccms.common.ide.jobs;

import static com.publiccms.common.ide.tools.NatureUtils.add;
import static com.publiccms.common.ide.tools.NatureUtils.set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class CovertJob extends Job {
    private IProject project;
    private String[] natures;
    private boolean append;

    public CovertJob(String name, IProject project, String[] natures) {
        this(name, project, natures, true);
    }

    public CovertJob(String name, IProject project, String[] natures, boolean append) {
        super(name);
        this.project = project;
        this.natures = natures;
        this.append = append;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            if (append) {
                return add(project, monitor, natures);
            } else {
                return set(project, monitor, natures);
            }
        } catch (CoreException e) {
            return new Status(IStatus.ERROR, "unknown", 1, e.getMessage(), e);
        }
    }
}