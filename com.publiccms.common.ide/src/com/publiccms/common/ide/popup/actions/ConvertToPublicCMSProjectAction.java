package com.publiccms.common.ide.popup.actions;

import static com.publiccms.common.ide.PublicCMSNature.NATURE_ID;
import static com.publiccms.common.ide.tools.NatureUtils.hasNature;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionDelegate;

import com.publiccms.common.ide.jobs.CovertJob;

public class ConvertToPublicCMSProjectAction extends BaseConvertAction {

    /**
     * 判断菜单禁用状态
     * 
     * @return
     */
    protected final boolean isEnabled() {
        for (IProject project : getProjects()) {
            if (!hasNature(project, NATURE_ID)) {
                return true;
            }
        }
        return !this.getProjects().isEmpty();
    }

    /**
     * 转换为Public CMS Template工程
     * 
     * @see IActionDelegate#run(IAction)
     */
    public void run(IAction action) {
        for (IProject project : getProjects()) {
            if (!hasNature(project, NATURE_ID)) {
                new CovertJob("Convert to Public CMS project", project, new String[] { NATURE_ID,
                        "org.eclipse.jdt.core.javanature" }).schedule();
            }
        }
    }

}
