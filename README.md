# MaterialList: Experiment with material design

This tiny app is a stub app to play with some Material design. The app simulates content moderation:

- Log into the app (enter any login and pass)
- Access to the list of media
- Moderate each media by allowing or deniying it

The idea behind this app is to be a playground for Material design implementation and AppCompat & support-design tests.

## Disclaimer

The app currently does nothing useful, it's just a playground with some cool animations.

Also, the app loads stub content from [Flickr](https://www.flickr.com/) so it may break if something change on that side.

## How it looks

Here's how the app looks:

![capture](capture.gif)

## Try it out

You can download a signed apk and try it on your device: [materialList-1.0.apk](apk/materialList-1.0.apk?raw=true)

## Dependencies

The app relies on some external tools:

- [ButterKnife](http://jakewharton.github.io/butterknife/): Library for view binding
- [Dagger 2](http://google.github.io/dagger/) && [android-apt](https://bitbucket.org/hvisser/android-apt): Dependency injections
- [Picasso](http://square.github.io/picasso/): Image loading, display and cache

### Licence

    Sources are availables under the Apache 2 licence (See LICENSE for details).