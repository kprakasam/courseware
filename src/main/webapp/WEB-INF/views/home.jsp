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
                var url = "search/" + $("#type")[0].value + "/" + request.term
                console.log(request.term)
                $.getJSON(url, request, function(data, status, xhr) {
                    response(data);
                });
            }
        });
    });
</script>
</head>
<body>
  <form method="get" action="select">
  <div class="ui-widget">
    <select id="type" name="type">
      <option value="course" selected>Course</option>
      <option value="assignment">Assignment</option>
    </select>
    <label for="searchTerm">: </label> <input id="search-term" />
  </div>
  </form>
</body>
</html>