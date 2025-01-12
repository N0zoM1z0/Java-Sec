#sql("query")
	select  
			 t.comment_id,
		 	 t.site_id,
		 	 t.content_id,
		 	 c.title_name,
		 	 t.comment_type,
		 	 t.comment_title,
		 	 t.comment_url,
		 	 t.comment_content,
		 	 t.comment_name,
		 	 t.comment_time,
		 	 t.comment_ip,
		 	 t.create_time,
		 	 t.check_status,
		 	 t.status,
		 	 t.remark
	from
		  of_cms_comment t left join of_cms_content c on t.content_id = c.content_id where t.site_id = #para(site_id) and t.status = '1'
	#if (title_name?? ) and  c.title_name like concat ('%', #para(title_name) ,'%')#end
	#if (sort?? && field) order by order_field order_sort  #else order by t.comment_id desc #end
#end
 
#sql("detail")
	select 
		 	 comment_id,
		 	 site_id,
		 	 content_id,
		 	 comment_type,
		 	 comment_title,
		 	 comment_url,
		 	 comment_content,
		 	 comment_name,
		 	 comment_time,
		 	 comment_ip,
		 	 create_time,
		 	 check_status,
		 	 status,
		 	 remark
	  from
		 of_cms_comment where  comment_id  = #para(comment_id)
#end

#sql("save")
	insert into of_cms_comment (
		 	 site_id,
		 	 content_id, 
		 	 comment_type, 
		 	 comment_title, 
		 	 comment_url, 
		 	 comment_content, 
		 	 comment_name, 
		 	 comment_time, 
		 	 comment_ip, 
		 	 create_time, 
		 	 check_status, 
		 	 status, 
		 	 remark 
	) values(
		 	 #para(site_id),
		 	 #para(content_id), 
		 	 #para(comment_type), 
		 	 #para(comment_title), 
		 	 #para(comment_url), 
		 	 #para(comment_content), 
		 	 #para(comment_name), 
		 	 now(),
		 	 #para(comment_ip), 
		 	 now(),
		 	 #para(check_status), 
		 	 '1',
		 	 #para(remark) 
	)
#end

#sql("delete")
	delete from of_cms_comment where  comment_id  = #para(comment_id)
#end

#sql("status")
	update  of_cms_comment set status = '0'  where comment_id  = #para(comment_id)
#end

#sql("update")
	update  
		of_cms_comment set 
			   comment_title = #para(comment_title),
			   comment_content = #para(comment_content),
			   comment_name = #para(comment_name), 
			   comment_ip = #para(comment_ip),
			   check_status = #para(check_status)
	where  comment_id  = #para(comment_id)
#end
 
#sql("list")
	select
		 	 comment_type,
		 	 comment_title,
		 	 comment_content,
		 	 comment_name,
		 	 comment_time,
		 	 comment_ip
	  from
		 of_cms_comment
		 where check_status = '1' and site_id = #para(site_id) and content_id = #para(content_id)  order by create_time desc

#end