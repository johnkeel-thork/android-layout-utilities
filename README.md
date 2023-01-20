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

## TextStroke.java
TextView with stroke around the text
Copy file to your code, plus the file res/values/attrs.xml

Use it in any of your layouts xml files, like the example at res/layout/text_stroke_example.xml

Change "com.thorkracing.dmd2_utils.TextStroke" to the path where you copied the TextStroke.java file

```
<com.thorkracing.dmd2_utils.TextStroke
        android:id="@+id/title_text"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_gravity="center"
        android:ellipsize="end"
        android:focusable="false"
        android:gravity="center"
        android:maxLength="20"
        android:maxLines="1"
        android:text="TESTING TEXT"
        android:textAlignment="gravity"
        android:textColor="@color/accent"
        app:autoSizeMaxTextSize="80sp"
        app:autoSizeMinTextSize="18sp"
        app:autoSizeStepGranularity="0.1sp"
        app:autoSizeTextType="uniform"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:stroke_color="@color/white"
        app:stroke_width="8" />
```
