package com.mykal.empublite;

/**
 * Created by mykal on 9/23/14.
 */
public class BookLoadedEvent {

    private BookContents contents = null;

    public BookLoadedEvent(BookContents contents) {
        this.contents = contents;
    }

    public BookContents getBook() {
        return(contents);
    }
}
