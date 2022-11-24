# Android Utilities
Some still relevant utility classes for Android Java

## SwipeListViewTouchListener.java
Make your ListView entries swipeable.
Copy file to your project and in your activity add the listener to your ListView like this:

```
SwipeListViewTouchListener touchListener =
    new SwipeListViewTouchListener(
            YOURLISTVIEW,
            new SwipeListViewTouchListener.OnSwipeCallback() {
                @Override
                public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {
                    // DO YOUR STUFF HERE - Get item position with reverseSortedPositions[0]
                }

                @Override
                public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {
                    // DO YOUR STUFF HERE - Get item position with reverseSortedPositions[0]
                }
            }, false, false);

YOURLISTVIEW.setOnTouchListener(touchListener);
YOURLISTVIEW.setOnScrollListener(touchListener.makeScrollListener());
```

## SwipeViewTouchListener.java
Just like the SwipListView but this one can be used with any view.
Copy file to your project and in your activity add the listener to your View like this:

```
SwipeViewTouchListener touchListener =
        new SwipeViewTouchListener(
                YOURVIEW,
                new SwipeViewTouchListener.OnSwipeCallback() {
                    @Override
                    public void onSwipeLeft(View view) {
                        // DO YOUR STUFF HERE - Example to make your view visibility gone:
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSwipeRight(View view) {
                        // DO YOUR STUFF HERE - Example to make your view visibility gone:
                        notificationBox.setVisibility(View.GONE);
                    }
                }, false, false);

YOURVIEW.setOnTouchListener(touchListener);
```
