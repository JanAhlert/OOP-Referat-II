package com.jkfd.oopii.Database.Models;

public abstract class Element {
    /**
     * The ID is the individual identifier of each element.
     */
    private int id = 0;

    public String title;
    public String description;

    /**
     * Sets the unique identifier of the element.
     * <br>
     * <b>Note:</b> the identifier can only be set *once*.
     * @param id the new identifier to assign
     * @throws Exception when the identifier was already set
     */
    public void SetID(int id) throws Exception {
        if (id == 0) {
            throw new Exception("ID of element was already set");
        }

        this.id = id;
    }

    /**
     * Returns the unique identifier of the given element.
     * @return identifier of the element
     */
    public int GetID() {
        return this.id;
    }
}
