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

  <div id="no-assignments-found"><p>No Assignments Found</p></div>
  
  <div id="assignments-grid-panel">
    <table id="assignments-grid"></table> 
    <div id="assignments-grid-pager"></div>
  </div>

  <div style="display:inline-block; height:20px"></div>

  <div id="no-jars-found"><p>No Jars Found</p></div>
  
  <div id="jars-grid-panel">
    <table id="jars-grid"></table> 
    <div id="jars-grid-pager"></div>
  </div>
  
  <script>
	$(function() {
	    var lastSelectedJarId = -1;
	    var lastSelectedAssignmentId = -1;

	    var isUploadInProgress = false;

	    var isJarsGridHidden = true;
	    var isAssignmentsGridHidden = true;
	    
	    $( "#jars-grid-panel" ).hide();
	    $( "#assignments-grid-panel" ).hide();
	    
	    $( "#no-jars-found" ).hide();
	    $( "#no-assignments-found" ).hide();
	    
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
			    refreshAssignmentsGrid(ui.item.value);
			}        
		});
          
		$( "#search-term" ).keydown(function( event ) {
			if ( event.which == 13 )
			    refreshAssignmentsGrid(this.value);
		});
		
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
				    refreshJarsGrid(id);
					lastSelectedAssignmentId=id; 
				}
			},
			gridComplete: function(d) {
			    if ( jQuery('#assignments-grid').jqGrid('getGridParam','records') == 0 )
			        hideAssignmentsGrid(); 
			    else
			        showAssignmentsGrid();
			},
			rowNum:10,
			rowList: [10,20,30],
			pager: "#assignments-grid-pager",
			caption: "Assignments"}
		).navGrid("#assignments-grid-pager",{ edit:false, add:false, del:false} ); 

		function refreshAssignmentsGrid(term) {
		  	if (isUploadInProgress)
				return;
  			    
			var url = "fetch-assignments/"+ $("#type")[0].value + "/" + encodeURIComponent(term);
			jQuery( "#assignments-grid" ).jqGrid('setGridParam',{url: url}).trigger("reloadGrid");			
		};
		
		function hideAssignmentsGrid() {
			if (!isAssignmentsGridHidden)
				return;

			isAssignmentsGridHidden = true;
		    $( "#assignments-grid-panel" ).hide();
		    $( "#no-assignments-found" ).show();
		} 
		
		function showAssignmentsGrid() {
			if (!isAssignmentsGridHidden)
				return;

			isAssignmentsGridHidden = false;
		    $( "#no-assignments-found" ).hide();
		    $( "#assignments-grid-panel" ).show();
		}

		jQuery( "#jars-grid" ).jqGrid( {
			datatype: "json",
			width: 600, 
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
					var ret = jQuery( "#jars-grid" ).jqGrid('getRowData',id);
					console.log("id="+ret.id+" Jar Name=" + ret.name + "...");
					lastSelectedJarId=id; 
				} 
			},
			gridComplete: function(d) {
			    if ( jQuery('#jars-grid').jqGrid('getGridParam','records') == 0 )
			        hideJarsGrid(); 
			    else
			        showJarsGrid();
			},
			rowNum:10,
			rowList: [10,20,30],
			pager: "#jars-grid-pager",
			caption: "Jars"}
		).navGrid("#jars-grid-pager",{ edit:false, add:false, del:false} );
		
		function refreshJarsGrid(assignmentId) {
			var url = "fetch-jars/"+ encodeURIComponent(assignmentId);
			jQuery( "#jars-grid" ).jqGrid('setGridParam',{url: url}).trigger("reloadGrid");			
		};
		
		function hideJarsGrid() {
			if (isJarsGridHidden)
				return;

			isJarsGridHidden = true;
		    $( "#jars-grid-panel" ).hide();
		    $( "#no-jarss-found" ).show();
		} 
		
		function showJarsGrid() {
			if (!isJaraGridHidden)
				return;

			isJarsGridHidden = false;
		    $( "#no-Jars-found" ).hide();
		    $( "#jars-grid-panel" ).show();
		}
	});
  </script>
</body>
</html>