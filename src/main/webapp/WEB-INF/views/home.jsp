<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>Courseware Home</title>

  <style>
   .ui-widget { font-size: 0.8em;}
  </style>    

  <link rel="stylesheet" href="resources/jquery-ui-1.10.3/css/jquery-ui-1.10.3.custom.min.css"></link>
  <link rel="stylesheet" href="resources/jqgrid-4.5.4/css/ui.jqgrid.css"></link>
  <script src="resources/jquery-ui-1.10.3/js/jquery-1.9.1.js"></script>
  <script src="resources/jquery-ui-1.10.3/js/jquery-ui-1.10.3.custom.min.js"></script>
  <script type="text/javascript" src="resources/jqgrid-4.5.4/js/i18n/grid.locale-en.js"></script>
  <script type="text/javascript" src="resources/jqgrid-4.5.4/js/jquery.jqGrid.min.js"></script>  
</head>

<body>
  <div class="ui-widget">
    <select id="search-type" name="type" class = "ui-autocomplete-input">
      <option value="course-code" selected>Course Code</option>
      <option value="course-name">Course Name</option>
      <option value="assignment-name">Assignment Name</option>
    </select>
    <label for="search-term">: </label> <input id="search-term" class="ui-autocomplete-input"/>
  </div>

  <div id="dialog"><p align="center"></p></div>

  <div style="display:inline-block; height:20px"></div>

  <div id="assignments-grid-panel">
    <table id="assignments-grid"></table> 
    <div id="assignments-grid-pager"></div>
  </div>

  <div style="display:inline-block; height:20px"></div>

  <div id="jars-grid-panel">
    <table id="jars-grid"></table> 
    <div id="jars-grid-pager"></div>
    <div id="jar-upload-pane">
      <span class="fileinput-button">
        <span>Add files...</span>
          <input type="file" name="files[]" multiple>
        </span>
    </div>
  </div>
  
  <script>
	$(function() {
	    var lastSelectedJarId = -1;
	    var lastSelectedAssignmentId = -1;

	    var isUploadInProgress = false;

	    var isJarsGridHidden = true;
	    var isAssignmentsGridHidden = true;
	    
	    var searchType = $( "#search-type" );
	    var searchTerm = $( "#search-term" );
	    
	    var dialogBox = $( "#dialog" );
	    var dialogBoxContent = $( "#dialog > p" );

	    var jarsGrid = $( "#jars-grid" );
	    var jarsGridPanel = $( "#jars-grid-panel" );
	    
	    var assignmentsGrid = $( "#assignments-grid" );
	    var assignmentsGridPanel = $( "#assignments-grid-panel" );
	    
	    var fileUpload = $('#fileupload');

	    var progressBars = [];
	    var deleteButtons = [];

	    jarsGridPanel.hide();
	    assignmentsGridPanel.hide();

	    dialogBox.dialog({
			width: 500,
			modal: true,
			autoOpen: false,
	        show: {
	        	effect: "fade",
	        	duration: 1000
	        },
	        hide: {
	        	effect: "fade",
	        	duration: 1000
	        }
		});
	    
		searchTerm.autocomplete({
			minLength : 2,
  			source : function( request, response ) {
  			  	if (isUploadInProgress)
  			  	    return;

				var url = "search/" + searchType.val() + "/" + encodeURIComponent(request.term);
				
				$.getJSON( url, null, function( data, status, xhr ) {
                      response( data );
				});
			},

			select: function( event, ui ) {
			    refreshAssignmentsGrid(ui.item.value);
			}        
		});
          
		searchTerm.keydown(function( event ) {
			if ( event.which == 13 )
			    refreshAssignmentsGrid(this.value);
		});
		
		assignmentsGrid.jqGrid( {
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
			    if ( jQuery('#assignments-grid').jqGrid('getGridParam','records') == 0 ) {
			        hideAssignmentsGrid();
				    showNoAssignmentsFound();
			    } else {
			        showAssignmentsGrid();
			    }
			},
			rowNum:10,
			rowList: [10,20,30],
			pager: "#assignments-grid-pager",
			caption: "Assignments"}
		).navGrid("#assignments-grid-pager",{ edit:false, add:false, del:false} ); 

		function refreshAssignmentsGrid(term) {
		  	if (isUploadInProgress)
				return;
  			    
			var url = "fetch-assignments/"+ searchType.val() + "/" + encodeURIComponent(term);
			assignmentsGrid.jqGrid('setGridParam',{url: url}).trigger("reloadGrid");			
		};
		
		function hideAssignmentsGrid() {
			if (isAssignmentsGridHidden)
				return;

			isAssignmentsGridHidden = true;
		    assignmentsGridPanel.hide();
		} 
		
		function showAssignmentsGrid() {
			if (!isAssignmentsGridHidden)
				return;

			isAssignmentsGridHidden = false;
		    assignmentsGridPanel.show();
		}

		function showNoAssignmentsFound() {
		    var content = "No assignments found for " + $("option:selected", searchType).text() + ' ' + searchTerm.val();
		    dialogBox.dialog( "option", "title", "No Assignments Found" );
		    dialogBoxContent.text(content);
		    dialogBox.dialog( "open" );
		}

		jarsGrid.jqGrid( {
			datatype: "json",
			width: 600, 
			height: 'auto',
			colNames:['Jar Id', 'Assignment Id','Jar Name', 'Main Class Fully Qualified Name', ''],
			colModel:[ 
				{name:'id',index:'id', hidden:true, width:5,},
				{name:'assignmentId',index:'assignmentId', hidden:true, width:5,},
				{name:'name',index:'name', width:30},
				{name:'mainClass',index:'mainClass', width:100},
				{name:'html', align:"center", formatter:htmlFormat, width:30}
			],
			onSelectRow: function(id) { 
				if(id && id !== lastSelectedJarId) {
					var ret = jarsGrid.jqGrid('getRowData',id);
					console.log("id="+ret.id+" Jar Name=" + ret.name + "...");
					lastSelectedJarId=id; 
				} 
			},
			gridComplete: function(d) {
			    if ( jQuery('#jars-grid').jqGrid('getGridParam','records') == 0 ) {
			        hideJarsGrid();
			        showNoJarssFound();
			    } else {
			        showJarsGrid();
			    }
			},
			rowNum:10,
			rowList: [10,20,30],
			pager: "#jars-grid-pager",
			caption: "Jars"}
		).navGrid("#jars-grid-pager",{ edit:false, add:false, del:false} );
		
		function htmlFormat( cellvalue, options, rowObject ){
		    if (cellValue = 'delete') {		        
		        var deleteButtonId = 'delete-button-' + rowObject.id;
		        deleteButtons.push(deleteButtonId);
		        return '<button id="' + deleteButtonId + '">delete</button>';
		    } else {
		        var progressBarId = 'delete-button-' + rowObject.id;
		        deleteButtons.push(progressBarId);
		        return '<button id="' + deleteButtonId + '">delete</button>';
		    }
		    
			return '<img src="'+cellvalue+'" />';
		}
		
		function refreshJarsGrid(assignmentId) {
			var url = "fetch-jars/"+ encodeURIComponent(assignmentId);
			jarsGrid.jqGrid('setGridParam',{url: url}).trigger("reloadGrid");			
		}
		
		function hideJarsGrid() {
			if (isJarsGridHidden)
				return;

			isJarsGridHidden = true;
		    jarsGridPanel.hide();
		} 
		
		function showJarsGrid() {
			if (!isJaraGridHidden)
				return;

			isJarsGridHidden = false;
		    jarsGridPanel.show();
		}
		
		function showNoJarssFound() {
		    var assignmentName = assignmentsGrid.jqGrid('getRowData', assignmentsGrid.jqGrid('getGridParam','selrow')).assignmentName;
		    var content = "No jars found for assignment " + assignmentName;
		    dialogBox.dialog( "option", "title", "No Jars Found" );
		    dialogBoxContent.text(content);
		    dialogBox.dialog( "open" );
		}
		
		fileUpload.fileupload({
		    url: 'jar-upload',
			dataType: 'json',
			dropZone: null,
			pasteZone: null,
	        add: function (e, data) {
	            data.context = $('<p/>').text('Uploading...').appendTo(document.body);
	            data.submit();
	        },
		});
	});
  </script>
</body>
</html>