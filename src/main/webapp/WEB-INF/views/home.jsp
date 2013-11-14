<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>Courseware Home</title>
  <link rel="stylesheet" href="resources/jquery-ui-1.10.3/css/jquery-ui-1.10.3.custom.min.css"></link>
  <link rel="stylesheet" href="resources/jqgrid-4.5.4/css/ui.jqgrid.css"></link>

  <script src="resources/jquery-ui-1.10.3/js/jquery-1.9.1.js"></script>
  <script src="resources/jquery-ui-1.10.3/js/jquery-ui-1.10.3.custom.min.js"></script>
  <script type="text/javascript" src="resources/jqgrid-4.5.4/js/i18n/grid.locale-en.js"></script>
  <script type="text/javascript" src="resources/jqgrid-4.5.4/js/jquery.jqGrid.min.js"></script>
</head>

<body>
  <div class="ui-widget">
    <select id="type" name="type" class = "ui-autocomplete-input">
      <option value="course-code" selected>Course Code</option>
      <option value="course-name">Course Name</option>
      <option value="assignment-name">Assignment Name</option>
    </select>
    <label for="searchTerm">: </label> <input id="search-term" class="ui-autocomplete-input"/>
  </div>

  <div style="display:inline-block; height:20px"></div>
  
  <div id="assignments-grid-panel">
    <table id="assignments-grid"></table> 
    <div id="assignments-grid-pager"></div>
  </div>

  <div style="display:inline-block; height:20px"></div>
  
  <div id="jar-file-grid-panel">
    <table id="jar-file-grid"></table> 
    <div id="jar-file-grid-pager"></div>
  </div>
  
  <script>
	$(function() {
	    var isUploadInProgress = false;
	    $( "#jar-file-grid-panel" ).hide();
	    $( "#assignments-grid-panel" ).hide();
	    
		$( "#search-term" ).autocomplete({
			minLength : 2,
  			source : function( request, response ) {
  			  	if (isUploadInProgress)
  			  	    return;

				var url = "search/" + $("#type")[0].value + "/" + encodeURIComponent(request.term);
				
				$.getJSON( url, null, function( data, status, xhr ) {
                      response( data );
				});
			},

			select: function( event, ui ) {
  			  	if (isUploadInProgress)
  			  	    return;
  			    
  			  	$( "#assignments-grid-panel" ).show();
  			  	var url = "fetch/"+ $("#type")[0].value + "/" + encodeURIComponent(ui.item.value);
				jQuery( "#assignments-grid" ).jqGrid('setGridParam',{url: url}).trigger("reloadGrid");
			}        
		});
          
		$( "#search-term" ).keydown(function( event ) {
			if (isUploadInProgress)
  				return;
			
			if ( event.which == 13 ) {
				var url = "fetch/"+ $("#type")[0].value + "/" + encodeURIComponent(this.value);
				jQuery( "#assignments-grid" ).jqGrid('setGridParam',{url: url}).trigger("reloadGrid");
			}
		});
		
		function displayAssignmentsGrid(assignments) {
		    // hide the table
	    	$( "#assignments-grid-panel" ).hide();
		    
		    // Delete any existing rows
		    
		    jQuery("#delgrid").jqGrid('delGridRow',gr,{reloadAfterSubmit:false});
			
		    //populate new data
		    for(var i=0; i<assignments.length; i++)
				jQuery( "#assignments-grid" ).jqGrid( 'addRowData', i+1, assignments[i] );
		    
		    $( "#assignments-grid-panel" ).show();
		};
		
	    var lastSelectedAssignmentId = -1;
	    
		jQuery( "#assignments-grid" ).jqGrid( {
			datatype: "json",
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
				if(id && id !== lastSelectedAssignmentId) {
					var ret = jQuery( "#assignments-grid" ).jqGrid('getRowData',id);
					console.log("id="+ret.id+" couse code=" + ret.courseCode + "...");
					lastSelectedAssignmentId=id; 
				} 
			},
			rowNum:10,
			rowList: [10,20,30],
			pager: "#assignments-grid-pager",
			caption: "Assignments"}
		).navGrid("#assignments-grid-pager",{ edit:false, add:false, del:false} ); 

	    var lastSelectedJarId = -1;
	    
		jQuery( "#jar-file-grid" ).jqGrid( {
			datatype: "local",
			width: 900, 
			height: 'auto',
			colNames:['Jar Id', 'Assignment Id','Jar Name', 'Main Class Fully Qualified Name'],
			colModel:[ 
				{name:'id',index:'id', hidden:true, width:5,},
				{name:'assignmentId',index:'assignmentId', hidden:true, width:5,},
				{name:'name',index:'name', width:30},
				{name:'mainClass',index:'mainClass', width:100}
			],
			onSelectRow: function(id) { 
				if(id && id !== lastSelectedJarId) {
					var ret = jQuery( "#jar-file-grid" ).jqGrid('getRowData',id);
					console.log("id="+ret.id+" Jar Name=" + ret.name + "...");
					lastSelectedJarId=id; 
				} 
			},
			rowNum:10,
			rowList: [10,20,30],
			pager: "#jar-file-grid-pager",
			caption: "Jars"}
		).navGrid("#jar-file-grid-pager",{ edit:false, add:false, del:false} ); 
	});
  </script>
</body>
</html>