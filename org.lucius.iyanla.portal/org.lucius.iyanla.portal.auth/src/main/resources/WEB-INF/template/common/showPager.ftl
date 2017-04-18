<div class="ui buttons">
	<input type="hidden" id="pageNo" name="pageNo" value="${pagination.pageNo}"/>
	<input type="hidden" id="pageSize" name="pageSize" value="${pagination.pageSize}"/>
	<input type="hidden" id="pageIndexSize" name="pageIndexSize" value="${pagination.pageIndex.perPageCount}"/>
	<span class=""><@spring.message "showPager.ftl.currentpage"/><b class="blue">${pagination.pageNo}</b>/<b class="blue">${pagination.totalPage}</b><@spring.message "showPager.ftl.currentpagepagenum"/></span>&nbsp;&nbsp;
	<#if pagination.firstPage>
		<a class="ui button"><@spring.message "showPager.ftl.firstpage"/></a>
		<a class="ui button"><@spring.message "showPager.ftl.prepage"/></a>
	<#else>
		<a class="ui button" href="javascript:;" onclick="_gotoPage('1');"><@spring.message "showPager.ftl.firstpage"/></a>
		<a class="ui button" href="javascript:;" onclick="_gotoPage('${pagination.prePage}');"><@spring.message "showPager.ftl.prepage"/></a>
	</#if>
	<#list pagination.pageIndex.startPageIndex .. pagination.pageIndex.endPageIndex as i>
		<#if pagination.pageNo==i>
			<a class="ui button visible">${i}</a>
		<#else>
			<a class="ui button" href="javascript:;" onclick="_gotoPage('${i}');">${i}</a>
	</#if>
	</#list>
	<#if pagination.lastPage>
		<a class="ui button"><@spring.message "showPager.ftl.nextpage"/></a>
		<a class="ui button"><@spring.message "showPager.ftl.lastpage"/></a>
	<#else>
		<a class="ui button" onclick="_gotoPage('${pagination.nextPage}');"><@spring.message "showPager.ftl.nextpage"/></a>
		<a class="ui button" onclick="_gotoPage('${pagination.totalPage}');"><@spring.message "showPager.ftl.lastpage"/></a>
	</#if>
		<span class="ml10 pnum"><@spring.message "showPager.ftl.goto"/></span>
		<input class="short" value="${pagination.pageNo}" type="text" id="toPageNo">
		<span class="pnum"><@spring.message "showPager.ftl.page"/></span>
		<a style="margin-right:20px" class="short" onclick="goto();"><@spring.message "showPager.ftl.gopage"/></a>
		<span  class="pnum"><@spring.message "showPager.ftl.perpage"/></span>
		<select id="chgPageSize" class="ui dropdown selection" tabindex="0">
			<option value="10" >10 <@spring.message "showPager.ftl.item"/></option>
			<option value="20" >20 <@spring.message "showPager.ftl.item"/></option>
			<option value="50" >50 <@spring.message "showPager.ftl.item"/></option>
		</select>
		
</div> 
<script type="text/javascript">
function goto(){
	var pageNo = $('#toPageNo').val();
	if(isNaN(pageNo)){
		_gotoPage(pageNo);
	}
}

function _gotoPage(pageNo) {
	try{
		var tableForm = document.getElementById("_pagination_form_");
		$("input[name=pageNo]").val(pageNo);
		tableForm.submit();
	} catch(e) {
		showMsg('_gotoPage(pageNo) method error');
	}
}

	$(function(){
		$("#toPageNo").blur(function() {
			var tableForm = document.getElementById("_pagination_form_");
			$("#pageNo").val($(this).val());
			tableForm.submit();
		});
		
		$("#chgPageSize").change(function(){
			var tableForm = document.getElementById("_pagination_form_");
			$("#pageSize").val($("#chgPageSize").val());
			tableForm.submit();
		});
		
		$("#chgPageSize").val(${pagination.pageSize});
	});
</script>