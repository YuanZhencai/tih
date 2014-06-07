package com.wcs.tih.transaction.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.tih.model.ProjectProblemmstr;

/**
 * Project: tih
 * Description: 
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
public class ProblemVO extends IdModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private ProjectProblemmstr problem;

    public ProblemVO() {}
    
    public ProblemVO(ProjectProblemmstr problem) {
        this.problem = problem;
        this.setId(problem.getId());
    }
    
    public ProjectProblemmstr getProblem() {
        return problem;
    }

    public void setProblem(ProjectProblemmstr problem) {
        this.problem = problem;
    }
}
