# MediaSelector 照片选择器、视频选择器
android image、video selector , easy to use \ simple design \n
Android 照片、视频选择器，简洁易用

# ScreenShot 样式
### List Page / Preview Page
### 照片列表页面 / 照片预览页面
<img src="https://github.com/Maxyjy/MediaSelector/assets/51241804/ee86334d-cd11-46f9-8bf5-0f29bc1bfe98" width = "300" align=center />
   
<img src="https://github.com/Maxyjy/MediaSelector/assets/51241804/882a992b-6341-4c7b-9217-497d5973b30b" width = "300" align=center />

# How To Use 如何使用
### 1.implementation 依赖添加
```gradle
repositories {
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.Maxyjy:MediaSelector:1.0.5'
}
```

### 2.open selector activity / 打开选择器 Activity
start `MediaSelectorListActivity` in your own activity
从Activity或Fragment中打开选择器Activity
```kotlin
selectImageButton.setOnClickListener {
   val intent = Intent(this, MediaSelectorListActivity::class.java)
   intent.putExtra(MediaSelectorExtras.MAX_SELECT_COUNT,YOUR_SELECT_COUNT)
   startActivityForResult(Intent(this, MediaSelectorListActivity::class.java), YOUR_REQUEST_CODE)
}
```

### 3.receive result / 接收选择照片或视频结果
override `onActivityResult` function in your Activity or Fragment to receive the result.
在Activity中onActivityResult方法接收选择结果
```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    //get the result of the selection
    if(requestCode == YOUR_REQUEST_CODE) {
      val mediaFiles = data?.getParcelableArrayListExtra<MediaFile>(MediaSelectorExtras.SELECTED_MEDIA_FILES)
    }
}

```
### 4.override color of Selector Pages (Optional) / 按你的项目需求设置页面颜色
define color in your own module to override selector page's color
声明同名Color资源来覆盖原有页面的颜色
```xml
    // checkbox color
    <color name="media_selector_checkbox_color">#2196F3</color>
    // toolbar background color
    <color name="media_selector_toolbar_background_color">#fff</color>
    // content background color
    <color name="media_selector_content_background_color">#fff</color>
    // text color
    <color name="media_selector_text_color">#000</color>
```
