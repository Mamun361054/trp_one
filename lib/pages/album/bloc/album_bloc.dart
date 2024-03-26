import 'dart:io';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:trp_one/gallery/models/album.dart';
import 'package:trp_one/gallery/photo_gallery.dart';
import 'package:trp_one/pages/album/bloc/album_state.dart';

class AlbumBloc extends Cubit<AlbumState> {

  AlbumBloc() : super(const AlbumState()){
    initAsync();
  }

  Future<bool> promptPermissionSetting() async {
    if (Platform.isIOS) {
      if (await Permission.photos
          .request()
          .isGranted || await Permission.storage
          .request()
          .isGranted) {
        return true;
      }
    }
    if (Platform.isAndroid) {
      if (await Permission.storage
          .request()
          .isGranted ||
          await Permission.photos
              .request()
              .isGranted &&
              await Permission.videos
                  .request()
                  .isGranted) {
        return true;
      }
    }
    return false;
  }

  Future<void> initAsync() async {
    emit(state.copyWith(loading: true));
    if (await promptPermissionSetting()) {
      List<Album> albums = await PhotoGallery.listAlbums();
      emit(state.copyWith(albums: albums, loading: false));
    }
  }
}