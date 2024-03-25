import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:transparent_image/transparent_image.dart';
import 'package:trp_one/gallery/image_providers/album_thumbnail_provider.dart';
import 'package:trp_one/pages/album/bloc/album_bloc.dart';

import 'album_image_screen.dart';

class AlbumScreen extends StatelessWidget {

  const AlbumScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return BlocProvider(
      create: (_) => AlbumBloc(context: context),
      child: Builder(
        builder: (context){

          final albumCubit = context.watch<AlbumBloc>();

          return Scaffold(
            appBar: AppBar(
              title: const Text('Albums'),
            ),
            body: albumCubit.state.loading
                ? const Center(
              child: CircularProgressIndicator(),
            ) : LayoutBuilder(
              builder: (context, constraints) {
                double gridWidth = (constraints.maxWidth - 20) / 3;
                double gridHeight = gridWidth + 33;
                double ratio = gridWidth / gridHeight;
                return Container(
                  padding: const EdgeInsets.all(5),
                  child: GridView.count(
                    childAspectRatio: ratio,
                    crossAxisCount: 3,
                    mainAxisSpacing: 5.0,
                    crossAxisSpacing: 5.0,
                    children: <Widget>[
                      ...albumCubit.state.albums.map((album) => GestureDetector(
                          onTap: () => Navigator.of(context).push(
                            MaterialPageRoute(builder: (context) => AlbumPage(album)),
                          ),
                          child: Column(
                            children: <Widget>[
                              ClipRRect(
                                borderRadius: BorderRadius.circular(5.0),
                                child: Container(
                                  color: Colors.grey[300],
                                  height: gridWidth,
                                  width: gridWidth,
                                  child: FadeInImage(
                                    fit: BoxFit.cover,
                                    placeholder: MemoryImage(kTransparentImage),
                                    image: AlbumThumbnailProvider(
                                      album: album,
                                      highQuality: true,
                                    ),
                                  ),
                                ),
                              ),
                              Container(
                                alignment: Alignment.topLeft,
                                padding: EdgeInsets.only(left: 2.0),
                                child: Text(
                                  album.name ?? "Unnamed Album",
                                  maxLines: 1,
                                  textAlign: TextAlign.start,
                                  style: TextStyle(
                                    height: 1.2,
                                    fontSize: 16,
                                  ),
                                ),
                              ),
                              Container(
                                alignment: Alignment.topLeft,
                                padding: EdgeInsets.only(left: 2.0),
                                child: Text(
                                  album.count.toString(),
                                  textAlign: TextAlign.start,
                                  style: TextStyle(
                                    height: 1.2,
                                    fontSize: 12,
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                );
              },
            ),
          );
        },
      ),
    );
  }
}
