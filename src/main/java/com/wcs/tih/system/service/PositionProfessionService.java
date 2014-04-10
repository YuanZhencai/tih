package com.wcs.tih.system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.CasUsrP;
import com.wcs.common.model.Dict;
import com.wcs.common.model.P;
import com.wcs.common.model.Usermstr;
import com.wcs.common.service.CommonService;
import com.wcs.tih.model.WfPosition;

/**
 * <p>Project: tih</p>
 * <p>Description: 岗位业务Service接口实现类</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Stateless
public class PositionProfessionService implements PositionProfessionInterface {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @PersistenceContext
    private EntityManager em;
    @EJB
    private CommonService commonService;


    private TreeNode taxationRoot;
    private List<TreeNode> totalTaxationNodes;

    private TreeNode documentTypeRoot;
    private List<TreeNode> totalDocNodes;

    /**
     * <p>Description: 初始化</p>
     */
    @PostConstruct
    public void init() {
        totalTaxationNodes = new ArrayList<TreeNode>();
        totalDocNodes = new ArrayList<TreeNode>();
    }

    /**
     * @see com.wcs.tih.system.service.PositionProfessionInterface#createTaxationTree(long)
     */
    @Override
    public TreeNode createTaxationTree(Locale browserLang, long positionId) {
        List<Dict> allDictTaxList = commonService.getDictByCat(DictConsts.TIH_TAX_TYPE, browserLang.toString());
        List<WfPosition> existTaxList = getAllPositionProfession(positionId, DictConsts.TIH_TAX_TYPE, 0);
        List<Dict> dictTaxList = null;
        if (allDictTaxList != null && allDictTaxList.size() != 0) {
            if (existTaxList != null && existTaxList.size() != 0) {
                boolean b = false;
                dictTaxList = new ArrayList<Dict>();
                for (Dict d : allDictTaxList) {
                    b = false;
                    for (WfPosition wp : existTaxList) {
                        if ((d.getCodeCat() + "." + d.getCodeKey()).equals(wp.getValue())) {
                            b = true;
                            break;
                        }
                    }
                    if (!b) {
                        dictTaxList.add(d);
                    }
                }
            } else {
                dictTaxList = allDictTaxList;
            }
        }
        taxationRoot = new DefaultTreeNode("root", null);
        totalTaxationNodes.clear();
        if (dictTaxList != null && dictTaxList.size() != 0) {
            List<WfPosition> taxList = getAllPositionProfession(positionId, DictConsts.TIH_TAX_TYPE, 1);
            for (Dict d : dictTaxList) {
                TreeNode tn = new DefaultTreeNode(d, taxationRoot);
                totalTaxationNodes.add(tn);
            }
            for (TreeNode tn : totalTaxationNodes) {
                Dict d = (Dict) tn.getData();
                if (taxList != null && taxList.size() != 0) {
                    for (WfPosition wp : taxList) {
                        if ((d.getCodeCat() + "." + d.getCodeKey()).equals(wp.getValue())) {
                            tn.setSelected(true);
                            break;
                        }
                    }
                }
            }
        }
        return taxationRoot;
    }

    /**
     * @see com.wcs.tih.system.service.PositionProfessionInterface#createDocumentTypeTree(long)
     */
    @Override
    public TreeNode createDocumentTypeTree(Locale browserLang, long positionId) {
        List<Dict> allDictDocList = commonService.getDictByCat(DictConsts.TIH_TAX_CAT, browserLang.toString());
        List<WfPosition> existDocList = getAllPositionProfession(positionId, DictConsts.TIH_TAX_CAT, 0);
        List<Dict> dictDocList = null;
        if (allDictDocList != null && allDictDocList.size() != 0) {
            if (existDocList != null && existDocList.size() != 0) {
                boolean b = false;
                dictDocList = new ArrayList<Dict>();
                for (Dict d : allDictDocList) {
                    b = false;
                    for (WfPosition wp : existDocList) {
                        if ((d.getCodeCat() + "." + d.getCodeKey()).equals(wp.getValue())) {
                            b = true;
                            break;
                        }
                    }
                    if (!b) {
                        dictDocList.add(d);
                    }
                }
            } else {
                dictDocList = allDictDocList;
            }
        }
        documentTypeRoot = new DefaultTreeNode("root", null);
        totalDocNodes.clear();
        if (dictDocList != null && dictDocList.size() != 0) {
            List<WfPosition> documentTypeList = getAllPositionProfession(positionId, DictConsts.TIH_TAX_CAT, 1);
            for (Dict d : dictDocList) {
                if (d.getCodeCat().equals(DictConsts.TIH_TAX_CAT)) {
                    TreeNode tn = new DefaultTreeNode(d, documentTypeRoot);
                    totalDocNodes.add(tn);
                    findSonResource(browserLang, d, tn, commonService.getDictByCat(d.getCodeCat() + "." + d.getCodeKey(), browserLang.toString()));
                }
            }
            for (TreeNode tn : totalDocNodes) {
                Dict d = (Dict) tn.getData();
                if (documentTypeList != null && documentTypeList.size() != 0) {
                    for (WfPosition wp : documentTypeList) {
                        if ((d.getCodeCat() + "." + d.getCodeKey()).equals(wp.getValue())) {
                            tn.setSelected(true);
                            break;
                        }
                    }
                }
            }
        }
        return documentTypeRoot;
    }

    /**
     * @throws Exception 
     * @see com.wcs.tih.system.service.PositionProfessionInterface#savePositionProfession(long, org.primefaces.model.TreeNode[], org.primefaces.model.TreeNode[])
     */
    @Override
    public void savePositionProfession(long positionId, TreeNode[] selectedTaxationNodes, TreeNode[] selectedDocumentTypeNodes) throws Exception {
        Dict d = null;
        WfPosition wp = null;
        // 保存税种
        boolean b = deletePositionProfession(positionId, DictConsts.TIH_TAX_TYPE);
        if (b) {
            if (selectedTaxationNodes != null && selectedTaxationNodes.length != 0) {
                for (TreeNode tn : selectedTaxationNodes) {
                    d = (Dict) tn.getData();
                    wp = new WfPosition();
                    wp.setPositionId(positionId);
                    wp.setType(d.getCodeCat());
                    wp.setValue(d.getCodeCat() + "." + d.getCodeKey());
                    try {
                        this.em.persist(wp);
                    } catch (Exception e) {
                        throw new Exception("系统异常，分配岗位业务税种失败");
                    }
                }
            }
        }
        // 保存文档类别
        boolean bb = deletePositionProfession(positionId, DictConsts.TIH_TAX_CAT);
        if (bb) {
            if (selectedDocumentTypeNodes != null && selectedDocumentTypeNodes.length != 0) {
                for (TreeNode tn : selectedDocumentTypeNodes) {
                    d = (Dict) tn.getData();
                    wp = new WfPosition();
                    wp.setPositionId(positionId);
                    wp.setType(d.getCodeCat());
                    wp.setValue(d.getCodeCat() + "." + d.getCodeKey());
                    try {
                        this.em.persist(wp);
                    } catch (Exception e) {
                        throw new Exception("系统异常，分配岗位业务文档类别失败");
                    }
                }
            }
        }
    }

    /**
     * <p>Description: 删除岗位的业务关系</p>
     * @param positionId 岗位ID
     * @param type 类型
     * @return 
     */
    private boolean deletePositionProfession(long positionId, String type) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete from WfPosition wp where 1=1");
        if (positionId != 0) {
            sb.append(" and wp.positionId=" + positionId + "");
        }
        if (type != null && !"".equals(type)) {
            sb.append(" and wp.type like '" + type + "%'");
        }
        boolean b = false;
        try {
            String sql = sb.toString();
            this.em.createQuery(sql).executeUpdate();
            b = true;
        } catch (Exception e) {
            b = false;
            logger.error(e.getMessage(), e);
        }
        return b;
    }

    /**
     * @see com.wcs.tih.system.service.PositionProfessionInterface#getUsermstrVo(java.lang.String)
     */
    @Override
    public List<UsermstrVo> getUsermstrVo(String codeCatKey) {
        StringBuffer sb = new StringBuffer();
        sb.append("select distinct u from Usermstr u,Userpositionorg upo,Positionorg po,Position position,WfPosition wp where u.id=upo.usermstr.id and upo.positionorg.id=po.id and po.position.id=position.id and position.id=wp.positionId");
        sb.append(" and wp.value ='" + codeCatKey + "'");
        sb.append(" and u.defunctInd='N' and upo.defunctInd='N' and po.defunctInd='N' and position.defunctInd='N'");
        List<Usermstr> usermstrs = this.em.createQuery(sb.toString()).getResultList();
        // 从TDS去上取得所有的用户
        List<UsermstrVo> usermstrVoList = null;
        if (null!=usermstrs && usermstrs.size() != 0) {
            usermstrVoList = new ArrayList<UsermstrVo>();
            UsermstrVo uv = null;
            long num = 0;
            for (Usermstr u : usermstrs) {
                num++;
                uv = new UsermstrVo();
                uv.setId(num);
                uv.setUsermstr(u);
                String sql = "select cup from CasUsrP cup where cup.id = '" + u.getAdAccount() + "'";
                List<CasUsrP> casUsrPs = this.em.createQuery(sql).getResultList();
                if (null != casUsrPs && casUsrPs.size() == 1) {
                    CasUsrP cup = casUsrPs.get(0);
                    uv.setCup(cup);
                    if (null != cup.getPernr() && !"".equals(cup.getPernr())) {
                        try {
                            uv.setP(this.em.find(P.class, cup.getPernr()));
                        } catch (Exception e) {
                            uv.setP(new P());
                            uv.getP().setNachn(u.getAdAccount());
                        }
                    } else {
                        uv.setP(new P());
                        uv.getP().setNachn(u.getAdAccount());
                    }
                }
                usermstrVoList.add(uv);
            }
        }
        return usermstrVoList;
    }

    /**
     * <p>Description: 递归生成树的子节点</p>
     * @param dict 字典对象
     * @param tn 父节点
     * @param dicts 
     */
    private void findSonResource(Locale browserLang,Dict dict, TreeNode tn, List<Dict> dicts) {
        for (Dict d : dicts) {
            if (d.getCodeCat().equals(dict.getCodeCat() + "." + dict.getCodeKey())) {
                TreeNode n = new DefaultTreeNode(d, tn);
                totalDocNodes.add(n);
                findSonResource(browserLang,d, n, commonService.getDictByCat(d.getCodeCat() + "." + d.getCodeKey(), browserLang.toString()));
            }
        }
    }

    /**
     * <p>Description: 取得所有的岗位业务</p>
     * @param positionId 岗位ID
     * @param type 类型
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<WfPosition> getAllPositionProfession(long positionId, String type, int positionFlag) {
        StringBuffer sb = new StringBuffer();
        sb.append("select wp from WfPosition wp where 1=1");
        if (positionId != 0 && positionFlag != 0) {
            sb.append(" and wp.positionId=" + positionId + "");
        }
        if (positionFlag == 0) {
            sb.append(" and wp.positionId <>" + positionId + "");
        }
        if (type != null && !"".equals(type)) {
            sb.append(" and wp.type like '" + type + "%'");
        }
        sb.append(" order by wp.type,wp.value");
        try {
            String sql = sb.toString();
            return this.em.createQuery(sql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
