package com.bookretail.export;

/**
 * NOTE: Store column names in static variable to reduce memory consumption!
 */
public interface IExportable {
    /**
     * Get column name
     *
     * @param i column index
     * @return column header
     */
    String getName(int i);

    /**
     * Get cell value
     *
     * @param i column index
     * @return cell value
     */
    Object getValue(int i);

    /**
     * Get column count
     *
     * @return column count
     */
    int getLength();
}
