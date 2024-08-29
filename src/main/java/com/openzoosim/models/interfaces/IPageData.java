package com.openzoosim.models.interfaces;

/**
 * All pages should at least have this data available to them on every page.
 * This will confirm that all base layouts and navbars, footers, etc have everything they need to load.
 */
public interface IPageData {
    public boolean IsLoggedIn();
}

