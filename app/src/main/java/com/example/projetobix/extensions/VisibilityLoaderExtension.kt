package com.example.projetobix.extensions

import androidx.core.view.isVisible
import com.example.projetobix.databinding.LoaderAnimationBinding

fun LoaderAnimationBinding.updateLoaderAnimationVisibility(isLoading: Boolean){
    if(isLoading){
        loaderAnimation.isVisible = true
        loaderAnimation.playAnimation()
    }else{
        loaderAnimation.isVisible = false
        loaderAnimation.cancelAnimation()
    }
}