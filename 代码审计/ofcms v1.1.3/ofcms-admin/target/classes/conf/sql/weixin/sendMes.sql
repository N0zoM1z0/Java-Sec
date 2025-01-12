#sql("query")
	select  
			 	 id,
		 	 template_key,
		 	 title,
		 	 firstclass,
		 	 secondclass,
		 	 content
	from
		  of_sys_weixin_template 
	#if (id?? ) where  id = #para(id)#end
	#if (sort?? && field) order by order_field order_sort  #else order by id desc #end
#end
 


#sql("detail")
	select 
		 	 id,
		 	 template_key,
		 	 title,
		 	 firstclass,
		 	 secondclass,
		 	 content
	  from
		 of_sys_weixin_template where  id  = #para(id)
#end

#sql("save")
	insert into of_sys_weixin_template (
		 	 id, 
		 	 template_key, 
		 	 title, 
		 	 firstclass, 
		 	 secondclass, 
		 	 content 
	) values(
		 	 #para(id), 
		 	 #para(template_key), 
		 	 #para(title), 
		 	 #para(firstclass), 
		 	 #para(secondclass), 
		 	 #para(content) 
	)
#end

#sql("delete")
	delete from of_sys_weixin_template where  id  = #para(id)
#end

#sql("status")
	update  of_sys_weixin_template set status = '0'  where id  = #para(id)
#end

#sql("update")
	update  
		of_sys_weixin_template set 
			   id = #para(id), 
			   template_key = #para(template_key), 
			   title = #para(title), 
			   firstclass = #para(firstclass), 
			   secondclass = #para(secondclass), 
			   content = #para(content) 
	where  id  = #para(id)
#end
 
