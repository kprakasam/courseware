<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>Courseware Home</title>
  <script src="resources/jquery-ui-1.10.3/js/jquery-1.9.1.js"></script>
  <script src="resources/jquery-ui-1.10.3/js/jquery-ui-1.10.3.custom.min.js"></script>
  <script type="text/javascript" src="resources/jqgrid-4.5.4/js/jquery.jqGrid.min.js"></script>
  <script type="text/javascript" src="resources/jqgrid-4.5.4/js/i18n/grid.locale-en.js"></script>
  
  <link rel="stylesheet" href="resources/jquery-ui-1.10.3/css/jquery-ui-1.10.3.custom.min.css"></link>
  <link rel="stylesheet" href="resources/jqgrid-4.5.4/css/ui.jqgrid.css"></link>
  
  <script>
      $(function() {
          $( "#search-term" ).autocomplete({
              minLength : 2,
              source : function( request, response ) {
                  var url = "search/" + $("#type")[0].value + "/" + encodeURIComponent(request.term);
                  $.getJSON( url, null, function( data, status, xhr ) {
                      response( data );
                  });
              },
              select: function( event, ui ) {
                  var url = "fetch/"+ $("#type")[0].value + "/" + encodeURIComponent(ui.item.value);
                  $( "#search-result" ).load( url );			
              }        
          });
          
          $( "#search-term" ).keydown(function( event ) {
              if ( event.which == 13 ) {
                  var url = "fetch/"+ $("#type")[0].value + "/" + encodeURIComponent(this.value);
                  $( "#search-result" ).load( url );			
  			}
  		});
      });
  </script>
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
  <div id="search-result"></div>

  <div style="display:inline-block; height:20px"></div>
  <div id="file-upload"></div>
</body>
</html>