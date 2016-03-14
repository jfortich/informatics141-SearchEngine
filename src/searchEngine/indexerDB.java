/**
 * INF 141 Information Retrieval
 * Ricky Fong 			# 821869
 * Jasmine Fortich		# 46446130
 * Natalie Kassir		# 14591873
 */

package searchEngine;

import java.beans.PropertyVetoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import searchEngine.DBConnectionManager;

public class indexerDB {
	
    public static final String URL  = "jdbc:mysql://localhost:3306/inf141index?autoReconnect=true&useSSL=false";
    public static final String USER = "root";
    public static final String PW 	= "Buster2000";

    
	// Updates the doc2docid table in the inf141index DB
	public static void updateDoc2DocID(String docName, Integer docID) throws PropertyVetoException {
		Connection con		  = null;
        PreparedStatement pst = null;

        try {

        	con = DBConnectionManager.getConnectionFromPool();

            pst = con.prepareStatement("INSERT IGNORE INTO doc2docid (docName, docID) VALUES(?, ?)");
            pst.setString(1, docName);
            pst.setInt(2, docID);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(indexerDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                	DBConnectionManager.returnConnectionToPool(con);
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(indexerDB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

	  }

	
	// updates the term2termid table in the inf141index DB
	public static void updateTerm2TermID(String term, Integer termID) throws PropertyVetoException {
		Connection con		  = null;
        PreparedStatement pst = null;

        try {

        	con = DBConnectionManager.getConnectionFromPool();

        	pst = con.prepareStatement("INSERT IGNORE INTO term2termid (term, termID) VALUES(?, ?)");
            pst.setString(1, term);
            pst.setInt(2, termID);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(indexerDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                	DBConnectionManager.returnConnectionToPool(con);
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(indexerDB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
	}

	
	// updates the termid2term table in the inf141index DB
	public static void updateTermID2Term(Integer termID, String term) throws PropertyVetoException {
		Connection con		  = null;
        PreparedStatement pst = null;
        
        try {

        	con = DBConnectionManager.getConnectionFromPool();

        	pst = con.prepareStatement("INSERT IGNORE INTO termid2term (termID, term) VALUES(?, ?)");
            pst.setInt(1, termID);
            pst.setString(2, term);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(indexerDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                	DBConnectionManager.returnConnectionToPool(con);
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(indexerDB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
	}	
	

	// Updates the doc2docid table in the inf141index DB
	public static void updateDocID2TermList(Integer docID, String termList) throws PropertyVetoException {
		Connection con		  = null;
        PreparedStatement pst = null;

        try {
        	
        	con = DBConnectionManager.getConnectionFromPool();
            
            pst = con.prepareStatement("INSERT IGNORE INTO docid2termlist (docID, termList) VALUES(?, ?)");
            pst.setInt(1, docID);
            pst.setString(2, termList);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(indexerDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                	DBConnectionManager.returnConnectionToPool(con);
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(indexerDB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
	}
	
	
	// updates the termfrequency table in the inf141index DB
	public static void updateTermFrequency(Integer termID, Integer docID, Integer frequency) throws PropertyVetoException {
		Connection con		  = null;
        PreparedStatement pst = null;

        try {
        	
        	con = DBConnectionManager.getConnectionFromPool();
            
            pst = con.prepareStatement("INSERT IGNORE INTO termfrequency (termID, docID, frequency) VALUES(?, ?, ?)");
            pst.setInt(1, termID);
            pst.setInt(2, docID);
            pst.setInt(3, frequency);
            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(indexerDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                	DBConnectionManager.returnConnectionToPool(con);
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(indexerDB.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
	}

	
	// Inserts values into termid2term table by retrieving
	// data from term2termid table
	public static void createTermID2Term () {
		Connection con		  = null;
        PreparedStatement pst = null;
        ResultSet rs 		  = null;

        try {

//            con = DriverManager.getConnection(URL, USER, PW);
        	
        	con = DBConnectionManager.getConnectionFromPool();
            
            pst = con.prepareStatement("SELECT * FROM term2termid");
            rs = pst.executeQuery();

            
            while (rs.next()) {
                String term = rs.getString(1);
                int termID 	= rs.getInt(2);

                updateTermID2Term(termID, term);
            }


        } catch (Exception ex) {
            Logger lgr = Logger.getLogger(indexerDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                	DBConnectionManager.returnConnectionToPool(con);
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(indexerDB.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
	}

	
	// Inserts values into termfrequency table by retrieving
	// data from term2termID table
	public static void createTermFrequency () {
        Connection con		  = null;
        PreparedStatement pst = null;
        ResultSet rs 		  = null;

        try {

//            con = DriverManager.getConnection(URL, USER, PW);

        	
        	con = DBConnectionManager.getConnectionFromPool();
            
            pst = con.prepareStatement("SELECT * FROM docid2termlist");
            rs = pst.executeQuery();

            
            while (rs.next()) {
                int docID 		= rs.getInt(1);
                String termList = rs.getString(2);
                
                String[] termIDs = termList.split(",");
                ArrayList<String> termIDList = new ArrayList<String>(Arrays.asList(termIDs));
                
                for (String termID : termIDList) {
    				int frequency = Collections.frequency(termIDList, termID);
    				int termIDint = Integer.parseInt(termID);
    				updateTermFrequency(termIDint, docID, frequency);
                }
                
            }


        } catch (Exception ex) {
            Logger lgr = Logger.getLogger(indexerDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                	DBConnectionManager.returnConnectionToPool(con);
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(indexerDB.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
	}
	
	
	
	
	// Retrieves the specified term's termID
	public static int retrieveTermID (String term) {
        Connection con 		  = null;
        PreparedStatement pst = null;
        ResultSet rs 		  = null;
        int termID 			  = 0;
        
        try {

//            con = DriverManager.getConnection(URL, USER, PW);
        	
        	con = DBConnectionManager.getConnectionFromPool();
            
            pst = con.prepareStatement("SELECT termID FROM term2termid WHERE term = '" + term + "'");
            rs = pst.executeQuery();

            
            while (rs.next()) {
                termID = rs.getInt(1);
            }
           

        } catch (Exception ex) {
            Logger lgr = Logger.getLogger(indexerDB.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                	DBConnectionManager.returnConnectionToPool(con);
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(indexerDB.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
        
		return termID;
	}


}