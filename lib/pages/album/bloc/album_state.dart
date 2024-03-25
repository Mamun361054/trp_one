import 'package:equatable/equatable.dart';
import 'package:trp_one/gallery/models/album.dart';

class AlbumState extends Equatable {
  final bool loading;
  final List<Album> albums;
  final String? selectedImage;

  const AlbumState(
      {this.loading = false, this.albums = const [], this.selectedImage});

  AlbumState copyWith({bool? loading, List<Album>? albums, String? selectedImage}) {
    return AlbumState(
        loading: loading ?? this.loading,
        albums: albums ?? this.albums,
        selectedImage: selectedImage ?? this.selectedImage);
  }

  @override
  List<Object?> get props => [loading, albums, selectedImage];
}
