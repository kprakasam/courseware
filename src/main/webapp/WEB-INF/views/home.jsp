<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>Courseware Home</title>

  <style>
    .ui-widget { 
      font-size: 0.8em;
    }
    
   .ui-th-column, .ui-jqgrid .ui-jqgrid-htable th.ui-th-column {
      text-align: left;
      white-space: nowrap;
    }
  </style>    

  <link rel="stylesheet" href="resources/jquery-ui-1.10.3/css/jquery-ui-1.10.3.custom.min.css"></link>
  <link rel="stylesheet" href="resources/jqgrid-4.5.4/css/ui.jqgrid.css"></link>
  <script src="resources/jquery-ui-1.10.3/js/jquery-1.9.1.js"></script>
  <script src="resources/jquery-ui-1.10.3/js/jquery-ui-1.10.3.custom.min.js"></script>
  <script type="text/javascript" src="resources/jqgrid-4.5.4/js/i18n/grid.locale-en.js"></script>
  <script type="text/javascript" src="resources/jqgrid-4.5.4/js/jquery.jqGrid.min.js"></script>
  <script type="text/javascript" src="resources/jQuery-File-Upload-9.2.1/js/jquery.iframe-transport.js"></script>
  <script type="text/javascript" src="resources/jQuery-File-Upload-9.2.1/js/jquery.fileupload.js"></script>
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

  <div id="info-dialog"><p align="center"></p></div>

  <div id="confirm-dialog" title="Delete Jar File?">
    <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>message</p>
  </div>

  <div style="display:inline-block; height:20px"></div>

  <div id="assignments-grid-panel">
    <table id="assignments-grid"></table> 
    <div id="assignments-grid-pager"></div>
  </div>

  <div style="display:inline-block; height:20px"></div>

  <div id="jars-grid-panel">
    <table id="jars-grid"></table> 
    <div id="jars-grid-pager"></div>
    <input id="jar-file-selector" type="file" name="files[]" multiple>
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
	    
	    var info = $( "#info-dialog" );
	    var infoDialogContent = $( "#info-dialog > p" );

	    var confirm = $( "#confirm-dialog" );
	    var confirmDialogContent = $( "#confirm-dialog > p" );

	    var jarsGrid = $( "#jars-grid" );
	    var jarsGridPanel = $( "#jars-grid-panel" );
	    
	    var assignmentsGrid = $( "#assignments-grid" );
	    var assignmentsGridPanel = $( "#assignments-grid-panel" );
	    
	    var fileUpload = $('#jar-file-selector');

	    var progressBars = [];

	    fileUpload.hide();
	    jarsGridPanel.hide();
	    assignmentsGridPanel.hide();

	    info.dialog({
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

	    confirm.dialog({
	        width: 500,
			modal: true,
			autoOpen: false,
			resizable: false,
			buttons: {
				"Delete": function() {					
				    var id = $( this ).data("id");
				    $( this ).removeData("id");
					$( this ).dialog( "close" );
					
					$.getJSON("delete-jar/" + encodeURIComponent(id), null, function( data, status, xhr ) {
	                     removeJarRecord(id);
					});
				},
			 
				Cancel: function() {
			 		$( this ).dialog( "close" );
			 	}
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
			height: 'auto',
			colNames:['id', 'Course Code','Course Name', 'Assignment Name', 'External Tool','Canvas Instance'],
			colModel:[ 
				{name:'id',index:'id', hidden:true, width:0,},
				{name:'courseCode',index:'courseCode', width:100},
				{name:'courseName',index:'courseName', width:200},
				{name:'assignmentName',index:'assignmentName', width:300},
				{name:'externalTool',index:'externalTool', width:200},
				{name:'canvasInstance',index:'canvasInstance', width:200}
			],
			onSelectRow: function(id) { 
				if(id && id !== lastSelectedAssignmentId) {
				    refreshJarsGrid(id);
					lastSelectedAssignmentId=id; 
				}
			},
			gridComplete: function() {
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
		).navGrid("#assignments-grid-pager",{ edit:false, add:false, del:false, refresh:false} ); 

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
		    info.dialog( "option", "title", "No Assignments Found" );
		    infoDialogContent.text(content);
		    info.dialog( "open" );
		}

		jarsGrid.jqGrid({
			datatype: "json",
			height: 200,
			colNames:['', '','Jar Name', 'Fully Qualified Main Class Name'],
			colModel:[ 
				{name:'id',index:'id', hidden:true, width:0},
				{name:'assignmentId',index:'assignmentId', hidden:true, width:0},
				{name:'name',index:'name', width:200, formatter:htmlFormat},
				{name:'mainClass',index:'mainClass', width:300}
			],
			onSelectRow: function(id) { 
				if(id && id !== lastSelectedJarId) {
					var ret = jarsGrid.jqGrid('getRowData',id);
					console.log("id="+ret.id+" Jar Name=" + ret.name + "...");
					lastSelectedJarId=id; 
				} 
			},
			gridComplete: function() {
			    showJarsGrid();
			},
			rowNum:10,
			rowList: [10,20,30],
			pager: "#jars-grid-pager",
			caption: "Jars"}
		).navGrid("#jars-grid-pager", {
		    add:true, 
		    del:true, 
		    edit:false,
		    refresh:false,
		    addfunc: function() {
		        fileUpload.click(); 
		    },
		    delfunc: function(id) {
		        showConfirmationDialog(id);
		    }
		});
		
		function htmlFormat( cellvalue, options, rowObject ){
		   /* if (cellValue = 'delete') {		        
		        var deleteButtonId = 'delete-button-' + rowObject.id;
		        deleteButtons.push(deleteButtonId);
		        return '<button id="' + deleteButtonId + '">delete</button>';
		    } else {
		        var progressBarId = 'delete-button-' + rowObject.id;
		        deleteButtons.push(progressBarId);
		        return '<button id="' + deleteButtonId + '">delete</button>';
		    } */
		    return cellvalue;
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
			if (!isJarsGridHidden)
				return;

			isJarsGridHidden = false;
		    jarsGridPanel.show();
		}
		
		function showConfirmationDialog(id) {
		    var jarName = jarsGrid.jqGrid('getRowData', id).name;
		    var content = "Do you want to delete '" + jarName + "'. Are you sure?";
		    confirm.dialog( "option", "width", content.length + 480);
		    confirmDialogContent.get(0).lastChild.nodeValue = content;
		    confirm.data("id", id);
		    confirm.dialog( "open" );
		}

		function removeJarRecord(id) {
			jarsGrid.jqGrid('delRowData',id);
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