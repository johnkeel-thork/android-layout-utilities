# Android Utilities
Some still relevant utility classes for Android Java

## SwipeListViewTouchListener.java
Make your ListView entries swipe-able.
Copy file to your project and in your activity add the listener to your ListView like this:

```
SwipeListViewTouchListener touchListener =
    new SwipeListViewTouchListener(
            YOURLISTVIEW,
            new SwipeListViewTouchListener.OnSwipeCallback() {
                @Override
                public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {
                    // DO YOUR STUFF HERE
                }

                @Override
                public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {
                    // DO YOUR STUFF HERE
                }
            }, false, false);

YOURLISTVIEW.setOnTouchListener(touchListener);
YOURLISTVIEW.setOnScrollListener(touchListener.makeScrollListener());
```
