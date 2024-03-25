import 'package:flutter/material.dart';
import 'package:transparent_image/transparent_image.dart';
import 'package:trp_one/gallery/image_providers/photo_provider.dart';
import 'package:trp_one/gallery/models/medium.dart';
import 'package:trp_one/gallery/photo_gallery.dart';

class ImageViewerPage extends StatelessWidget {
  final Medium medium;
  const ImageViewerPage(this.medium, {super.key});

  @override
  Widget build(BuildContext context) {
    DateTime? date = medium.creationDate ?? medium.modifiedDate;
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          leading: IconButton(
            onPressed: () => Navigator.of(context).pop(),
            icon: const Icon(Icons.arrow_back_ios),
          ),
          title: date != null ? Text(date.toLocal().toString()) : null,
        ),
        body: Container(
          alignment: Alignment.center,
          child: GestureDetector(
            onTap: () async {
              PhotoGallery.deleteMedium(mediumId: medium.id);
            },
            child: FadeInImage(
              fit: BoxFit.cover,
              placeholder: MemoryImage(kTransparentImage),
              image: PhotoProvider(mediumId: medium.id),
            ),
          ),
        ),
      ),
    );
  }
}