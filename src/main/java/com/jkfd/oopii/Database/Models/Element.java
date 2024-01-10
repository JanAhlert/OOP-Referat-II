package com.jkfd.oopii.Database.Models;

public abstract class Element {
    /**
     * The ID is the individual identifier of the specific element.
     */
    private int id = 0;

    /**
     * The title of the specific element. This will be displayed in many places.
     */
    public String title;

    /**
     * The description of the specific element. This will most likely be displayed in detail views.
     */
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
