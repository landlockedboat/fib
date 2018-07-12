%% L6 - Morfologia
I = imread('Wheel.bmp');
G = rgb2gray(I);
BW = G > 20;
SE = strel('disk',8);
EI = imerode(BW,SE);
OI = imopen(BW,SE);
CI = imclose(BW,SE);
DI = imdilate(BW,SE);
%imshow(EI);
%figure;imshow(OI);
%figure;imshow(CI);
%figure;imshow(DI);

EIC = BW*255-EI*128;
imshow(EIC,[]);
OIC = BW*255-OI*128;
imshow(OIC,[]);
CIC = BW*255-CI*128;
imshow(CIC,[]);
DIC = BW*255-DI*128;
imshow(DIC,[]);

BM = bwmorph(BW,'remove');
% 'skel' esquelet
% 'srink' ultimate erode
imshow(BM);

%% Exercici: Separar grans amb erode
I = imread('cafe.tif');
BW = I < 16;
% imshow(BW);
% Ara cal separar objectes 'dèbilment' connectats
SE = strel('disk',9);
EI = imerode(BW,SE);
imshow(EI);

%% Exercici: Distance transform
I = imread('cafe.tif');
BW = I < 16;
DT = bwdist(1-BW, 'euclidean');
% imshow(DT, []);
BDT = DT > 8.5;
imshow(BDT, []);
% Llavors fem un connected components i a casa

%% Apunts
% 1- Hem de fer un desto de connected componens amb la diferencia de la
% imatge closed i la original

% 2- Hem de binaritzar, segmentar i contar-les i marcar-les amb insert
% marker. Les celules de les vores suda. Nomes celules completes.
% imborderextraction o algo aixi esborra les bvores 

% imagereconstruction reconstrueix la imatge des de una imatge sense bvores 
% tambe

% hard mode pots fer un altre processat per les celules de les vores (+ 20 min no)

% 3- Marcar la cel mes aillada. Es pot mesurar dist amb el centre de mases
% o amb la min distancia desde la pell de la celula (NO ES POT FER FORS)
% (NO ES POT JARCHODEKJAR EL vOR MARTI JOD3R)
% (ES D'ENGINY) (((((<10 linies for sure))))
% (IDeIA FELiS)
% (sTART PROJECT PlsSSSS)

%% Exercici 1:
I = imread('Wheel.bmp');
G = rgb2gray(I);
BWu = G > 20;
BW = imfill(BWu,'holes');
SE = strel('disk',4);
ER = imopen(BW, SE);
R = BW - ER;
C = bwconncomp(R);
npixels = cellfun(@numel,C.PixelIdxList);
npues = sum(npixels > 5);

%% Exercici 2 (codi de classe):
I = imread('normal-blood1.jpg');
G = rgb2gray(I);
BWu = G < 200;
BW = imfill(BWu,'holes');

BWB = false(size(BW));
BWB(1,:) = true; BWB(:,1) = true;
BWB(end,:) = true; BWB(:,end) = true;

IR = imreconstruct(BWB,BW);
BW = BW - IR;
DT = bwdist(1-BW, 'euclidean');
imshow(DT, []);
BDT = DT > 30;
imshow(BDT, []);
C = bwconncomp(BDT);
npixels = cellfun(@numel,C.PixelIdxList);
[x,ncells] = size(npixels);
S = regionprops(C,'centroid');

f = @(pos) insertMarker(I, pos.Centroid);
arrayfun(f, S);
imshow(I);

%% Exercici 2 (Hamster):
I = imread('normal-blood1.jpg');
Ig = rgb2gray(I);
BW = Ig < 180;
BW = imfill(BW, 'holes');

DT = bwdist(1-BW, 'euclidean');
BW2 = DT > 6;

BW2 = bwareaopen(BW2,20);

%imshowpair(BW, BW2, 'montage');

cc = bwconncomp(BW2);
s = regionprops(cc, 'Centroid');
centroids = cat(1, s.Centroid);

I = insertMarker(I, centroids);
imshow(I);

