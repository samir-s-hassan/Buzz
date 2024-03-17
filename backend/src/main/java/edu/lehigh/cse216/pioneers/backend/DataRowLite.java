package edu.lehigh.cse216.pioneers.backend;

/**
 * DataRowLite is for communicating back a subset of the information in a
 * DataRow.  Specifically, we only send back the id and idea.  Note that
 * in order to keep the client code as consistent as possible, we ensure 
 * that the field names in DataRowLite match the corresponding names in
 * DataRow.  As with DataRow, we plan to convert DataRowLite objects to
 * JSON, so we need to make their fields public.
 * 
 * CHANGED for mMessage to mIdea
 */
public class DataRowLite {
    /**
     * The id for this row; see DataRow.mId
     */
    public int mId;

    /**
     * The idea string for this row of data; see DataRow.mIdea
     */
    public String mIdea;

    /**
     * Create a DataRowLite by copying fields from a DataRow
     * @param data The DataRow object from which to copy fields
     */
    public DataRowLite(DataRow data) {
        this.mId = data.mId;
        this.mIdea = data.mIdea;
    }
}