package com.publiccms.common.ide.popup.actions;

import java.util.LinkedHashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkingSet;

public abstract class BaseConvertAction implements IObjectActionDelegate {
    private LinkedHashSet<IProject> projects = new LinkedHashSet<IProject>();

    protected abstract boolean isEnabled();

    /**
     * 选中项变化时，获取当前选中的Project
     * 
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        if ((selection instanceof IStructuredSelection)) {
            this.projects = new LinkedHashSet<IProject>();
            IStructuredSelection s = (IStructuredSelection) selection;
            if (!s.isEmpty()) {
                for (Object element : s.toArray()) {
                    if ((element instanceof IProject)) {
                        this.projects.add((IProject) element);
                    } else if ((element instanceof IResource)) {
                        this.projects.add(((IResource) element).getProject());
                    } else {
                        IResource resource;
                        if ((element instanceof IAdaptable)) {
                            IAdaptable adaptable = (IAdaptable) element;
                            IProject project = (IProject) adaptable.getAdapter(IProject.class);
                            if (project == null) {
                                resource = (IResource) adaptable.getAdapter(IResource.class);
                                if (resource != null) {
                                    project = resource.getProject();
                                }
                            }

                            if (project != null) {
                                this.projects.add(project);
                            }
                        } else if ((element instanceof IWorkingSet)) {
                            IWorkingSet workingSet = (IWorkingSet) element;
                            IAdaptable[] arrayOfIAdaptable;
                            int length = (arrayOfIAdaptable = workingSet.getElements()).length;
                            for (int i = 0; i < length; i++) {
                                IAdaptable adaptable = arrayOfIAdaptable[i];
                                IProject project = (IProject) adaptable.getAdapter(IProject.class);
                                if (project != null) {
                                    this.projects.add(project);
                                } else {
                                    resource = (IResource) adaptable.getAdapter(IResource.class);
                                    if ((resource != null) && (resource.getProject() != null)) {
                                        this.projects.add(resource.getProject());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // 判断菜单禁用状态
        action.setEnabled(isEnabled());
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    public LinkedHashSet<IProject> getProjects() {
        return projects;
    }
}
