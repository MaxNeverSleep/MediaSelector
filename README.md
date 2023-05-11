# MediaSelector
android image、video selector
easy to use \ simple design

# ScreenShot
### List Page  /  Preview Page
<img src="https://github.com/Maxyjy/MediaSelector/assets/51241804/ee86334d-cd11-46f9-8bf5-0f29bc1bfe98" width = "300" align=center />
   
<img src="https://github.com/Maxyjy/MediaSelector/assets/51241804/882a992b-6341-4c7b-9217-497d5973b30b" width = "300" align=center />

# How To Use
### 1.implementation
```gradle
repositories {
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.Maxyjy:MediaSelector:1.0.5'
}
```

### 2.open selector activity
start `MediaSelectorListActivity` in your own activity
```kotlin
selectImageButton.setOnClickListener {
   val intent = Intent(this, MediaSelectorListActivity::class.java)
   intent.putExtra(MediaSelectorExtras.MAX_SELECT_COUNT,YOUR_SELECT_COUNT)
   startActivityForResult(Intent(this, MediaSelectorListActivity::class.java), YOUR_REQUEST_CODE)
}
```

### 3.receive result
override `onActivityResult` function in your Activity or Fragment to receive the result.
```kotlin
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //get the result of the selection
        if(requestCode == YOUR_REQUEST_CODE) {
          val mediaFiles = data?.getParcelableArrayListExtra<MediaFile>(MediaSelectorExtras.SELECTED_MEDIA_FILES)
        }
    }
```
