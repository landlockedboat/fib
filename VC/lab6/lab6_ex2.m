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
[,ncells] = size(npixels);
S = regionprops(C,'centroid');
centroids = cat(1, S.Centroid);
I = insertMarker(I, centroids);
imshow(I);