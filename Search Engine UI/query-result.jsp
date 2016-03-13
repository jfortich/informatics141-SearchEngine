<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.sql.*" %>
<% Class.forName("sun.jdbc.odbc.JdbcOdbcDriver"); %>

<html>
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

      <title>Informatics 141 Search Engine</title>
    </head>

    <body>
        <h1 class=center>Top Result(s)</h1>

        <% 
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inf141index?autoReconnect=true&useSSL=false", "root", "Buster2000");
            Statement statement = connection.createStatement();

            // Retrieve query from Web UI
            String query        = request.getParameter("query");  
            String[] queryTerms = query.split("\\s+");

            // If the query contains multiple terms
            if ( queryTerms.size() > 1) {
              String termIDs = new StringBuilder();

              // Goes through list of query terms and grabs their termIDs
              for (String term : queryTerms) {
                  ResultSet resultset = statement.executeQuery("SELECT termID FROM termid2term WHERE term = '" + term + "'") ; 
                  termIDs.concat(resultset.getInt(1) + ",");
              }

            }

            // if the query contains a single term
            else {
              // Retrieve the term's termID
              ResultSet resultset = statement.executeQuery("SELECT termID FROM termid2term WHERE term = '" + term + "'") ; 
              int termID          = resultSet.getInt(1);

              // Retrieves the top 10 queries in descending order frequency
              ResultSet resultset = statement.executeQuery("SELECT docID FROM termfrequency WHERE termID = '" + termID + "' AND frequency >= 1 ORDER BY frequency DESC LIMIT 10") ; 
            
              // If no queries found, print out message
              if(!resultset.next()) {
                out.println("Sorry, could not find any results for '" + query + "'");
              } 

              // If queries found use docID to retrieve docName
              else {
                while (resultset.next()) {
                  int docID      = resultSet.getInt(1);
                  int frequency  = resultSet.getInt(2); 
                  resultset      = statement.executeQuery("SELECT * FROM doc2docid WHERE docID = '" + docID + "'") ''
        %>

        <table border="1">
            <tr>
               <th>DocID </th>
               <th>Doc Name </th>
           </tr>
           <tr>
               <td> <%= resultset.getInt(1) %> </td>
               <td> <%= resultset.getString(2) %> </td>
           </tr>
       </table>
       <br>
       <%   }
           } 
       %>
    </body>
</html>