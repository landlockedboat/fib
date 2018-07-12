%% Classe de la sessió 5

%% Segmentació
% segmentacio és agrupar els pizels que preserven una coherencia entre
% ells. Partim de les separacions binaris (conectivitat,propietats,divisió
% recursiva).

%% Conectivitat
I = imread('money.tif');
imshow(I);
BW = im2bw(I,95/255);
C = bwconncomp(BW);
imshow(BW);

J = I;
J(C.PixelIdxList{1}) = 0;
imshow(J);
% nombre the conjunts o cosos
n = numel(C.PixelIdxList);
% nombre de pixels a cada conjunts
npixels = cellfun(@numel,C.PixelIdxList);
% struct de les posicions en pantalla (x,y)
S = regionprops(C,'centroid');
% boundingbox ortogonal als eixos
S = regionprops(C,'centroid','BoundingBox');

%% Atenuar el nivell de gris de la moneda més gran
I = imread('money.tif');
BW = im2bw(I,95/255);
C = bwconncomp(BW);
npixels = cellfun(@numel,C.PixelIdxList);
[V,P] = max(npixels);
I(C.PixelIdxList{P}) = I(C.PixelIdxList{P}) * .25;
imshow(I);

%% Agrupació de pixels per color
% c = kmeans(>observacions<,>nombre de classes a trobar<);
I = imread('street1.jpg');
HSV = rgb2hsv(I);
%imshow(HSV);
H = HSV(:,:,1);
S = HSV(:,:,2);
V = HSV(:,:,3);
% passem 2D -> 1D pel kmeans
O = [H(:), S(:), V(:)]; 
C = kmeans(O,8);
% passem 1D -> 2D pel plot
IC = reshape(C,size(H));
%imshow(IC,[]);
imshowpair(HSV,IC,'montage');

%% Segmentació per Split and Merge
I = imread('forest.jpg');
I = rgb2gray(I);
S = qtdecomp(I,0.7, 16);

blocks = repmat(uint8(0),size(S));

for dim = [512 256 128 64 32 16 8 4 2 1];    
  numblocks = length(find(S==dim));    
  if (numblocks > 0)        
    values = repmat(uint8(1),[dim dim numblocks]);
    values(2:dim,2:dim,:) = 0;
    blocks = qtsetblk(blocks,S,dim,values);
  end
end

blocks(end,1:end) = 1;
blocks(1:end,end) = 1;

imshow(blocks,[])
% S = qtdecomp(I,@myfunction);
% desviació estàndard molt gran vol dir diferència de colors --> dividir
% desviació estàndard molt petita vol dir mateixos colors --> no dividir

%% KMEANS


% c = kmeans(>observacions<,>nombre de classes a trobar<);
I = imread('heat.png');
HSV = rgb2hsv(I);
%imshow(HSV);
H = HSV(:,:,1);
S = HSV(:,:,2);
V = HSV(:,:,3);
% passem 2D -> 1D pel kmeans
O = [H(:), S(:), V(:)]; 
C = kmeans(O,4);
% passem 1D -> 2D pel plot
IC = reshape(C,size(H));
%imshow(IC,[]);

I1 = IC == 1;
I2 = IC == 2;
I3 = IC == 3;
I4 = IC == 4;

CONN1 = bwconncomp(I1);

%imshowpair(HSV,IC,'montage');
NP1 = cellfun(@numel,CONN1.PixelIdxList);

[V,P1] = max(NP1);
F1 = IC;
F1(CONN1.PixelIdxList{P1}) = 0;
imshowpair(I1,F1,'montage');
