import 'package:flutter_test/flutter_test.dart';
import 'package:trp_one/pages/album/bloc/album_state.dart';

main() {
  group('Album state properties test', () {
    AlbumState albumState = const AlbumState();

    test('Return false for loading state', () {
      expect(albumState.loading, false);
    });

    test('Return empty for albums state', () {
      expect(albumState.albums, []);
    });

    test('Return null for selectedImage state', () {
      expect(albumState.selectedImage, null);
    });

    test('Support state equality', () {
      expect(albumState, equals(const AlbumState()));
    });

    test('Support vale equality', () {
      expect(const AlbumState().props, albumState.props);
    });
  });
}
