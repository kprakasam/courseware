<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:choose>
  <c:when test="${fn:length(assignments) == 0}">
    <p>No Assignments Found.</p> 
  </c:when>
  <c:otherwise>    
    <table id="grid"></table> 
    <div id="pager"></div>

    <c:forEach var="assignment" items="${assignments}" varStatus="status">
      <c:set var="element" value="{ id: ${assignment.id}, courseCode: '${assignment.courseCode}', courseName: '${assignment.courseName}', assignmentName: '${assignment.name}', externalTool: '${assignment.externalTool}', canvasInstance: '${assignment.canvasInstance}' }" /> 
      <c:choose>
          <c:when test="${status.first}">
            <c:set var="dataArray" value="${element}" />
          </c:when>
          <c:otherwise>
            <c:set var="dataArray" value="${dataArray}, ${element}" />      
          </c:otherwise>
      </c:choose>
    </c:forEach>
    
    <script type="text/javascript">
		$(function() {
		    var state = { lastSelectedId : -1 };
		    
			jQuery( "#grid" ).jqGrid( {
				datatype: "local",
  				width: 900, 
  				height: 'auto',
				colNames:['id', 'Course Code','Course Name', 'Assignment Name', 'External Tool','Canvas Instance'],
				colModel:[ 
					{name:'id',index:'id', hidden:true, width:5,},
					{name:'courseCode',index:'courseCode', width:30},
					{name:'courseName',index:'courseName', width:80},
					{name:'assignmentName',index:'assignmentName', width:80},
					{name:'externalTool',index:'externalTool', width:50},
					{name:'canvasInstance',index:'canvasInstance', width:50}
				],
				onSelectRow: function(id) { 
					if(id && id !== state.lastSelectedId) {
						var ret = jQuery( "#grid" ).jqGrid('getRowData',id);
						console.log("id="+ret.id+" couse code=" + ret.courseCode + "...");
						state.lastSelectedId=id; 
					} 
				},
				rowNum:10,
				rowList: [10,20,30],
				pager: "#pager",
				caption: "Assignments"}
			).navGrid("#pager",{ edit:false, add:false, del:false} ); 
          
			var assignments = [<c:out escapeXml="false" value="${dataArray}"/>]; 
          
			for(var i=0; i<assignments.length; i++) {
				jQuery( "#grid" ).jqGrid( 'addRowData', i+1, assignments[i] );
			}
		});
	</script>
  </c:otherwise>
</c:choose>  