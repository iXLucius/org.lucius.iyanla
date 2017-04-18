<title>首页</title>
<table class="ui right aligned celled table">
  <thead>
    <tr>
        <th class="left aligned">序号</th>
        <th>用户名</th>
        <th>性别</th>
        <th>职业</th>
        <th>密码</th>
        <th>年龄</th>
  	</tr>
  </thead>
  <tbody>
  	<#if users?? && users?size &gt; 0>
		<#list users as u>
			<tr>
	          <td class="left aligned">${u_index+1}</td>
	          <td class="positive">${u.username!}</td>
	          <td>男</td>
	          <td>Java Engineer</td>
	          <td class="negative">${u.password!}</td>
	          <td class="warning">${u.age!}</td>
	        </tr>
		</#list>
	<#else>
		<tr align="center">
			<td colspan="10">
				暂无数据
			</td>
		</tr>
	</#if>
  </tbody>
</table>