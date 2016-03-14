<%@ page import="java.sql.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.HashMap" %>

<HTML>
    <HEAD>
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

      <title>Informatics 141 Search Engine</title>
    </HEAD>

    <BODY>
      <% String query = request.getParameter("query"); %>
      <h1 class="center results-title">Top 10 result(s) for "<%= query %>" </h1>

        <% 
            // Database credentials
            String databaseURL   = "jdbc:mysql://localhost:3306/inf141index?autoReconnect=true&useSSL=false";
            String databaseUSER  = "root";
            String databasePW    = "Buster2000";

            String[] terms = query.split("\\s+");

            // If the query has multiple terms
            HashMap<String, Integer> term2ID = new HashMap<String, Integer>(); 
            if (terms.length > 1) {
                Class.forName("com.mysql.jdbc.Driver").newInstance(); 
                Connection connection = DriverManager.getConnection(databaseURL, databaseUSER, databasePW);
                Statement statement   = connection.createStatement() ;

                String termIDQuery          = "SELECT termID FROM termid2term WHERE term = '" + terms[0] + "'";
                StringBuilder selectTermIDs = new StringBuilder(termIDQuery);

                for (int i=1; i < terms.length; i++) {
                    selectTermIDs.append(" UNION (SELECT termID FROM termid2term WHERE term = '" + terms[i] + "')");
                }

                String selectDocIDs = "SELECT distinct(docID), docName FROM termfrequency NATURAL JOIN doc2docid WHERE termID IN (" + selectTermIDs.toString() +") AND frequency >= 1 ORDER BY frequency DESC LIMIT 10";
                ResultSet resultset = statement.executeQuery(selectDocIDs) ; 
       %>

            <TABLE CLASS="table table-striped table-hover table-condensed">
                <TR>
                    <TH>DOC ID</TH>
                    <TH>DOC NAME</TH>
                </TR>
                <% while(resultset.next()){ %>
                <TR>
                    <TD> <%= resultset.getInt(1) %></td>
                    <TD> <%= resultset.getString(2) %></TD>
                </TR>
                <% } %>
            </TABLE>
        <%
            }

            // If the query is a single term
            else {
                String term = query;
                Class.forName("com.mysql.jdbc.Driver").newInstance(); 
                Connection connection = DriverManager.getConnection(databaseURL, databaseUSER, databasePW);
                Statement statement   = connection.createStatement() ;

                String selectTermID = "(SELECT termID FROM termid2term WHERE term = '" + term + "')";
                String selectDocIDs = "SELECT docID, docName FROM termfrequency NATURAL JOIN doc2docid WHERE termID = "+ selectTermID +" AND frequency >= 1 ORDER BY frequency DESC LIMIT 10";
                ResultSet resultset = statement.executeQuery(selectDocIDs) ; 
        %>

            <TABLE CLASS="table table-striped table-hover table-condensed">
                <TR>
                    <TH>DOC ID</TH>
                    <TH>DOC NAME</TH>
                </TR>
                <% while(resultset.next()){ %>
                <TR>
                    <TD> <%= resultset.getInt(1) %></td>
                    <TD> <%= resultset.getString(2) %></TD>
                </TR>
                <% } } %>
            </TABLE>

    <a class="btn btn-default btn-lg col-md-6 col-md-offset-3 search-again-button" href="search-engine-index.jsp"> 
        <span class="glyphicon glyphicon-repeat" aria-hidden="true"></span> Search Again
    </a>

    </BODY>
</HTML>
