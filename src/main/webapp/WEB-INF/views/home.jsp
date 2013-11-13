<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8" />
<title>Courseware Home</title>
<script src="resources/js/jquery-1.9.1.js"></script>
<script src="resources/js/jquery-ui-1.10.3.custom.min.js"></script>
<link rel="stylesheet" href="resources/css/jquery-ui-1.10.3.custom.min.css"></link>
<script>
    $(function() {
        $("#search-term").autocomplete({
            minLength : 2,
            source : function(request, response) {
                var url = "search/" + $("#type")[0].value + "/" + request.term;
                $.getJSON(url, null, function(data, status, xhr) {
                    response(data);
                });
            },
            select: function( event, ui ) {
                var url = "get/"+ $("#type")[0].value + "/" + request.term
                $( "#result" ).load( "/test.html" );  
            }        
        });
    });
</script>
</head>
<body>
  <div class="ui-widget">
    <select id="type" name="type">
      <option value="course-code" selected>Course Code</option>
      <option value="course-name" selected>Course Name</option>
      <option value="assignment-name">Assignment Name</option>
    </select>
    <label for="searchTerm">: </label> <input id="search-term" />
  </div>
  <div id="search-result" class="ui-widget">
  </div>
</body>
</html>