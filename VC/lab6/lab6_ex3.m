I = imread('normal-blood1.jpg');
G = rgb2gray(I);
BW = G < 200;
BW = imfill(BW,'holes');
BW = imopen(BW, strel('disk', 3));

BWB = false(size(BW));
BWB(1,:) = true; BWB(:,1) = true;
BWB(end,:) = true; BWB(:,end) = true;
IR = imreconstruct(BWB,BW);
BW = BW - IR;

BW = bwulterode(BW);
DT = bwdist(BW, 'euclidean');
DT = -DT;
imshow(DT, []);

[A, idx] = max(DT(:));
[i, j] = ind2sub(size(I), idx);

I = insertMarker(I, [i j], 'o', 'color', 'black', 'size', 50);

imshow(I,[]);