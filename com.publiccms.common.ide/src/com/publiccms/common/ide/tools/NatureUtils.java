package com.publiccms.common.ide.tools;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class NatureUtils {
    /**
     * 添加Nature
     * 
     * @param project
     * @param reqNatures
     * @throws CoreException
     */
    public static IStatus add(IProject project, IProgressMonitor monitor, String[] reqNatures) throws CoreException {
        IProjectDescription desc = project.getDescription();
        String[] oldNaturesArr = desc.getNatureIds();
        Set<String> natures = new LinkedHashSet<String>();
        for (String n : reqNatures) {
            natures.add(n);
        }
        for (String n : oldNaturesArr) {
            natures.add(n);
        }
        desc.setNatureIds(natures.toArray(new String[natures.size()]));
        if (natures.size() > oldNaturesArr.length) {
            project.setDescription(desc, monitor);
        } else {
            project.setDescription(desc, 64, monitor);
        }
        return Status.OK_STATUS;
    }

    /**
     * 添加Nature
     * 
     * @param project
     * @param reqNatures
     * @throws CoreException
     */
    public static IStatus set(IProject project, IProgressMonitor monitor, String[] reqNatures) throws CoreException {
        IProjectDescription desc = project.getDescription();
        Set<String> natures = new LinkedHashSet<String>();
        for (String n : reqNatures) {
            natures.add(n);
        }
        desc.setNatureIds(natures.toArray(new String[natures.size()]));
        project.setDescription(desc, monitor);
        return Status.OK_STATUS;
    }

    /**
     * 移除Nature
     * 
     * @param project
     * @param natureId
     * @throws CoreException
     */
    public static IStatus remove(IProject project, IProgressMonitor monitor, String natureId) throws CoreException {
        IProjectDescription desc = project.getDescription();
        String[] oldNaturesArr = desc.getNatureIds();
        Set<String> natures = new LinkedHashSet<String>();
        for (String n : oldNaturesArr) {
            if (!n.equals(natureId)) {
                natures.add(n);
            }
        }
        if (natures.size() != oldNaturesArr.length) {
            desc.setNatureIds(natures.toArray(new String[natures.size()]));
            project.setDescription(desc, monitor);
        }
        return Status.OK_STATUS;
    }

    /**
     * 判断是否存在Nature
     * 
     * @param p
     * @param natureId
     * @return
     */
    public static boolean hasNature(IProject p, String natureId) {
        try {
            return (p != null) && (p.isAccessible()) && (p.hasNature(natureId));
        } catch (CoreException e) {
            return false;
        }
    }
}