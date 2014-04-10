package com.wcs.tih.system.service;

import java.util.List;
import java.util.Locale;

import javax.ejb.Local;

import org.primefaces.model.TreeNode;

import com.wcs.common.controller.vo.UsermstrVo;

/**
 * <p>Project: tih</p>
 * <p>Description: 岗位业务service接口类</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Local
public interface PositionProfessionInterface {
    
    /**
     * <p>Description: 创建税种树</p>
     * @param positionId 岗位ID
     * @return 税种树节点
     */
    public TreeNode createTaxationTree(Locale browserLang,long positionId);
    
    /**
     * <p>Description: 创建文档类别树</p>
     * @param positionId 岗位ID
     * @return 文档类别节点
     */
    public TreeNode createDocumentTypeTree(Locale browserLang,long positionId);
    
    /**
     * <p>Description: 保存岗位业务信息</p>
     * @param positionId 选中的岗位ID
     * @param selectedTaxationNodes 选中的税种节点 
     * @param selectedDocumentTypeNodes 选中的文档类别节点
     * @return 消息字符串
     */
    public void savePositionProfession(long positionId, TreeNode[] selectedTaxationNodes, TreeNode[] selectedDocumentTypeNodes)throws Exception;
	
    /**
	 * <p>Description: 根据税种或文档类别取得用户</p>
	 * @param codeCatKey 税种或文档类别
	 * @return 用户Vo对象 没有返回null
	 */
	public List<UsermstrVo> getUsermstrVo(String codeCatKey);
}
