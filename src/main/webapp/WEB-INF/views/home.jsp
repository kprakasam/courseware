<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>Courseware Home</title>
  <link rel="stylesheet" href="resources/jquery-ui-1.10.3/css/cupertino/jquery-ui-1.10.3.custom.min.css"></link>
  <link rel="stylesheet" href="resources/jqgrid-4.5.4/css/ui.jqgrid.css"></link>
  <script src="resources/jquery-ui-1.10.3/js/jquery-1.9.1.js"></script>
  <script src="resources/jquery-ui-1.10.3/js/jquery-ui-1.10.3.custom.min.js"></script>
  <script type="text/javascript" src="resources/jqgrid-4.5.4/js/i18n/grid.locale-en.js"></script>
  <script type="text/javascript" src="resources/jqgrid-4.5.4/js/jquery.jqGrid.min.js"></script>
  <script type="text/javascript" src="resources/jQuery-File-Upload-9.2.1/js/jquery.iframe-transport.js"></script>
  <script type="text/javascript" src="resources/jQuery-File-Upload-9.2.1/js/jquery.fileupload.js"></script>

  <style>
    .ui-widget { 
      font-size: 1em;
    }
    
   .ui-th-column, .ui-jqgrid .ui-jqgrid-htable th.ui-th-column {
      text-align: left;
      white-space: nowrap;
    }
  </style>    

</head>

<body>
  <div class="ui-widget">
    <select id="search-type" name="type">
      <option value="course-code" selected>Course Code</option>
      <option value="course-name">Course Name</option>
      <option value="assignment-name">Assignment Name</option>
    </select>
    <label for="search-term">: </label> <input id="search-term"/>
  </div>

  <div id="info-dialog" class="ui-widget"><p align="center"></p></div>

  <div id="confirm-dialog" class="ui-widget" title="Delete Jar File?">
    <p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>message</p>
  </div>

  <div style="display:inline-block; height:20px"></div>

  <div id="assignments-grid-panel" class="ui-widget">
    <table id="assignments-grid"></table> 
  </div>

  <div style="display:inline-block; height:20px"></div>

  <div id="jars-grid-panel" class="ui-widget">
    <table id="jars-grid"></table> 
    <div id="jars-grid-pager"></div>
    <input id="jar-file-selector" type="file" name="file" multiple>
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

	    var jarId = 0;
	    var progressBarMax = 10;

	    fileUpload.hide();
	    jarsGridPanel.hide();
	    assignmentsGridPanel.hide();

	    info.dialog({
			width: 500,
			modal: true,
			autoOpen: false,
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
			rowNum: 10,
			height: 'auto',
			scrollOffset: 0,
			hidegrid: false,
			datatype: "local",
			caption: "Assignments",
			colNames:['Course Code','Course Name', 'Assignment Name', 'External Tool','Canvas Instance'],
			colModel:[ 
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
			}
		}); 

		function refreshAssignmentsGrid(term) {
		  	if (isUploadInProgress)
				return;
  			    
			var url = "fetch-assignments/"+ searchType.val() + "/" + encodeURIComponent(term);

			$.getJSON( url, null, function( data, status, xhr ) {
			    if ( data.length === 0) {
			        hideJarsGrid();
			        hideAssignmentsGrid();
				    showNoAssignmentsFound();
			    } else {
					assignmentsGrid.clearGridData();
					assignmentsGrid.addRowData('id', data);
			        showAssignmentsGrid();
			    }
			});
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
		
		function showDuplicateFileDialog(filename) {
		    var content = "Jar File '"+ filename + "' has been uploaded already. Please select someother file";
		    info.dialog( "option", "title", "Duplicate Jar File" );
		    infoDialogContent.text(content);
		    info.dialog( "open" );

		}
		
		jarsGrid.jqGrid({
			rowNum:10,
			height: 150,
			scrollOffset: 0,
			caption: "Jars",
			pginput: false,
			hidegrid: false,
			pgbuttons: false,
			datatype: "local",
			pager: "#jars-grid-pager",
			colNames:['Jar Name', 'Fully Qualified Main Class Name'],
			colModel:[ 
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
		}).navGrid("#jars-grid-pager", {
		    add:true, 
		    del:true, 
		    edit:false,
		    refresh:false,
		    alertcap:'No Jar Selected',
		    alerttext: 'Please select a jar file to delete',
		    addfunc: function() {
		        fileUpload.click(); 
		    },
		    delfunc: function(id) {
		        showConfirmationDialog(id);
		    }
		});
		
		function htmlFormat( cellvalue, options, rowObject ){
		   if (rowObject.addProgressBar) {		       
		        return '<div><span style="float: left;">' + cellvalue + '</span><div id="progressbar'+ rowObject.id +'"></div></div>';
		    } else {
		        return cellvalue;
		    } 		    
		}
		
		function refreshJarsGrid(assignmentId) {
			var url = "fetch-jars/"+ encodeURIComponent(assignmentId);

			$.getJSON( url, null, function( data, status, xhr ) {
			    jarsGrid.clearGridData();
			    jarsGrid.addRowData('id', data);
			    showJarsGrid();
			});
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
			jarsGrid.delRowData(id);
		}
		
		$.widget('blueimp.fileupload', $.blueimp.fileupload, {
			options: {
				acceptFileTypes: /(\.|\/)(jar)$/i,
		        processQueue: [
					{ 
						action: 'validate',
						acceptFileTypes: '@'
					}
				]
		    },

		    processActions: {
		        validate: function (data, options) {
		            var dfd = $.Deferred();
		            var file = data.files[data.index];
		            
		            var files = $.grep(jarsGrid.getRowData(), function(r, i){
		                if (r.name === file.name) return r;
		            });

		            if (files.length > 1) {
		                showDuplicateFileDialog(fileName);
		                dfd.rejectWith(this, [data]);
		                return dfd.promise();
		            }

		            files = $.grep(data.files, function(f, i) {
		                if (f.name === file.name) return f; 
		            });
		            
		            if (files.length > 1) {
		                showDuplicateFileDialog(fileName);
		                dfd.rejectWith(this, [data]);
		                return dfd.promise();
		            }            
		            
		            rowData = {
		                id : (--jarId).toString(),
	                    name : file.name,
	                    mainClass: '',
	                    addProgressBar: true,
	                    file: file                 
	                };
		            
		            jarsGrid.addRowData('id', [rowData]);
		            
		            var progressBar = $( "#progressbar" + rowData.id  );
		            
		            progressBar.progressbar({
		                value: 1,
		                max : progressBarMax,
		            });
		            
		            data.context = progressBar;
		            dfd.resolveWith(this, [data]);
		            return dfd.promise();
		        }
		    }
		});
		
		fileUpload.fileupload({
		    url: 'upload-jar',
			dataType: 'json',
			dropZone: null,
			pasteZone: null,			
			progress: function(e, data) {
				var progress = parseInt(data.loaded / (data.total / progressBarMax));
				data.context.progressbar("option", "value", progress);
			},
			done: function(e, data) {
			    data.context.progressbar("destroy");
			},
			add: function (e, data) {
	            data.submit();
	        },
		});		
	});
  </script>
</body>
</html>