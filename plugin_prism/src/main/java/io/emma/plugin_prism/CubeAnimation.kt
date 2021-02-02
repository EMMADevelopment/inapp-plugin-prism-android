package io.emma.plugin_prism

import android.view.animation.Transformation


open class CubeAnimation (val direction: Direction,
                                 val enter: Boolean,
                                 private val timeDuration: Long): ViewPropertyAnimation() {

    init {
        duration = timeDuration
    }
    companion object {

        internal class VerticalCubeAnimation(direction: Direction, enter: Boolean, duration: Long) :
                CubeAnimation(direction, enter, duration) {

           override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
               super.initialize(width, height, parentWidth, parentHeight)
               mPivotX = width * 0.5f
               mPivotY = if (enter == ( direction == Direction.UP)) 0.0f else height.toFloat()
               mCameraZ = -height * 0.015f;
           }

           override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
               var value = if (enter) interpolatedTime - 1.0f else interpolatedTime;
               if (direction == Direction.DOWN) value *= -1.0f;
               mRotationX = value * 90.0f;
               mTranslationY = -value * mHeight;

               super.applyTransformation(interpolatedTime, t);
               applyTransformation(t);
           }
        }

        internal class HorizontalCubeAnimation(direction: Direction, enter: Boolean, timeDuration: Long) :
                CubeAnimation(direction, enter, timeDuration) {

            override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
                super.initialize(width, height, parentWidth, parentHeight)
                mPivotX = if (enter == ( direction == Direction.LEFT)) 0.0f else width.toFloat()
                mPivotY = height * 0.5f;
                mCameraZ = -width * 0.015f;
            }

            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                var value = if (enter) (interpolatedTime - 1.0f) else interpolatedTime;
                if (direction == Direction.RIGHT) value *= -1.0f;
                mRotationY = -value * 90.0f;
                mTranslationX = -value * mWidth;

                super.applyTransformation(interpolatedTime, t);
                applyTransformation(t);
            }
        }

        fun create(direction: Direction, enter: Boolean, duration: Long): CubeAnimation {
            return when (direction) {
                Direction.UP, Direction.DOWN -> VerticalCubeAnimation(direction, enter, duration)
                Direction.LEFT, Direction.RIGHT -> HorizontalCubeAnimation(direction, enter, duration)
                else -> HorizontalCubeAnimation(direction, enter, duration)
            }
        }
    }
}