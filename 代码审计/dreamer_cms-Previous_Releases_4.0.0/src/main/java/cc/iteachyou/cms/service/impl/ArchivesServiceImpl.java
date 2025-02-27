package cc.iteachyou.cms.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cc.iteachyou.cms.common.SearchEntity;
import cc.iteachyou.cms.dao.ArchivesMapper;
import cc.iteachyou.cms.entity.Archives;
import cc.iteachyou.cms.entity.ArchivesWithRownum;
import cc.iteachyou.cms.entity.vo.ArchivesVO;
import cc.iteachyou.cms.exception.TransactionException;
import cc.iteachyou.cms.service.ArchivesService;

/**
 * 文章管理处理类
 * @author LIGW
 */
@Service
@Transactional(rollbackFor = Exception.class) 
public class ArchivesServiceImpl implements ArchivesService {

	@Autowired
	private ArchivesMapper archivesMapper;
	/**
	 * 添加
	 * @throws TransactionException 
	 */
	@Override
	public int save(Archives archives,String tableName, Map<String,Object> additional) throws TransactionException {
		int num = 0;
		try {
			num = archivesMapper.insertSelective(archives);
			if(additional.size() > 0) {
				archivesMapper.insertAdditional(tableName, additional);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动事务回滚
			throw new TransactionException(e.getMessage());
		}
		return num;
	}
	
	/**
	 * 列表
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	@Override
	public PageInfo<Map<String, Object>> queryListByPage(SearchEntity params) {
		if(params.getPageNum() == null || params.getPageNum() == 0) {
			params.setPageNum(1);
		}
		if(params.getPageSize() == null || params.getPageSize() == 0) {
			params.setPageSize(10);
		}
		Map<String, Object> entity = params.getEntity();
		/**
		 * 修复后台SQL注入风险
		 */
		if(entity.containsKey("tableName")) {
			entity.remove("tableName");
		}
		PageHelper.startPage(params.getPageNum(), params.getPageSize());
		List<Map<String, Object>> list = archivesMapper.queryListByPage(entity);
		PageInfo<Map<String, Object>> page = new PageInfo<Map<String, Object>>(list);
		return page;
	}

	/**
	 * 根据标签名查询文章
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	@Override
	public List<Archives> queryListByTagName(String tagName) {
		return archivesMapper.queryListByTagName(tagName);
	}

	@Override
	public int saveAdditional(String tableName, Map<String, Object> params) {
		return archivesMapper.insertAdditional(tableName, params);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	@Override
	public Map<String,Object> queryArticleById(Map<String, Object> params) {
		return archivesMapper.queryArticleById(params);
	}
	
	@Override
	public int update(Archives archives,String tableName, Map<String,Object> additional,String fid) throws TransactionException {
		int num = 0;
		try {
			num = archivesMapper.updateByPrimaryKeySelective(archives);
			if(additional.size() > 0) {
				archivesMapper.updateAdditional(tableName, additional,fid);	
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动事务回滚
			throw new TransactionException(e.getMessage());
		}
		return num;
	}
	
	@Override
	public int delete(String id,Map<String,Object> params) throws TransactionException {
		int num = 0;
		try {
			num= archivesMapper.deleteByPrimaryKey(id);
			archivesMapper.deleteAdditional(params);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动事务回滚
			throw new TransactionException(e.getMessage());
		}
		return num;
	}

	@Override
	public int updateTagById(Archives article) {
		return archivesMapper.updateTagByPrimaryKey(article);
	}

	@Override
	public int updateAdditional(String tableName, Map<String, Object> additional,String fid) {
		return archivesMapper.updateAdditional(tableName, additional,fid);
	}

	@Override
	public int deleteAdditional(Map<String, Object> params) {
		return archivesMapper.deleteAdditional(params);
	}

	@Override
	public Archives selectByPrimaryKey(String id) {
		return this.archivesMapper.selectByPrimaryKey(id);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	@Override
	public PageInfo<ArchivesVO> queryListByKeywords(SearchEntity params) {
		if(params.getPageNum() == null || params.getPageNum() == 0) {
			params.setPageNum(1);
		}
		if(params.getPageSize() == null || params.getPageSize() == 0) {
			params.setPageSize(10);
		}
		PageHelper.startPage(params.getPageNum(), params.getPageSize());
		List<ArchivesVO> list = this.archivesMapper.queryListByKeywords(params.getEntity());
		PageInfo<ArchivesVO> page = new PageInfo<ArchivesVO>(list);
		return page;
	}

	@Override
	public ArchivesWithRownum queryArticleRowNum(Map<String, Object> params) {
		return this.archivesMapper.queryArticleRowNum(params);
	}

	@Override
	public List<Archives> queryListByTop() {
		return archivesMapper.queryListByTop();
	}

	@Override
	public int update(Archives archives) {
		return archivesMapper.updateByPrimaryKeySelective(archives);
	}

	@Override
	public List<Archives> queryAll(String id) {
		return archivesMapper.queryAll(id);
	}
	
}
