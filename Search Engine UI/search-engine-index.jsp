<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8"/>

    <!-- Fonts -->
    <link href='https://fonts.googleapis.com/css?family=Roboto:400,100,300,700' rel='stylesheet' type='text/css'>

    <!-- CSS -->
    <link href="css/search-engine-stylesheet.css" rel="stylesheet" type="text/css" >

    <!-- Jquery -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" integrity="sha512-dTfge/zgoMYpP7QbHy4gWMEGsbsdZeCXz7irItjcC3sPUFtf0kuFbDz/ixG7ArTxmDjLXDmezHubeNikyKGVyQ==" crossorigin="anonymous" />

    <!-- Bootstrap JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js" integrity="sha512-K1qjQ+NcF2TYO/eI3M6v8EiNYZfA95pQumfvcVrTHtwQVDG+aHRqLi/ETn2uB+1JqwYqVG3LIvdm9lj6imS/pQ==" crossorigin="anonymous"></script>

    <title> INF 141 Search Engine</title>
</head>

<body>
	<div class="row search">
		<div class="col-md-6 col-md-offset-3">
			<h1 class="center"> 
				<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
				Informatics 141 Search Engine 
			</h1>
			<p class="center"> Search for information on the UCI ICS server! </p>
			<form action="query-result.jsp" method="POST">
				<div class="input-group">
					<input id="query" name="query" type="text" class="form-control" placeholder="Search for ICS stuff...">
					<span class="input-group-btn">
						<button class="btn btn-default" type="SUBMIT" value="Submit">Go!</button>
					</span>
				</div><!-- /input-group -->
			</form>
			<p class="credit-text center"> A project by Ricky Fong, Jasmine Fortich, and Natalie Kassir</p>
		</div><!-- /.col-lg-6 -->
	</div><!-- /.row -->
</body>

</html>