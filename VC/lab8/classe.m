%% Esquema Clássic de classificació d'imatges
%
% Imatge 
% Preprocessat(filtrar soroll, resaltar rellevancies)
% Binaritzat (d'aquells elements importants)
% Segmentació (agrupar els objectes)
% Caracteristiques (extreure característiques de diferents objectes)
% Predictor (obtenir un vector d'observacions i classificar-les)
% Aprenentage (usant treebaggers o altres estructures)
%
%% PREDICT
clc, clear all
% Fase I - Train
I = rgb2gray(imread('Chess figures-12-plusHP.png'));
% ^ this means 12 times the six figures and then the horse-pawn diff
BW = I < 128;
BW = medfilt2(BW,[5,5]);
BW = medfilt2(BW,[5,5]);
BW = bwareaopen(BW,40);
BW = imfill(BW,'holes');
CC = bwconncomp(BW);
imshow(BW);
% hem de obtenir característiques resistents a canvis com
% rotacions i zooms
ext = cell2mat(struct2cell(regionprops(CC,'Extent')));
ecc = cell2mat(struct2cell(regionprops(CC,'Eccentricity')));
conv = cell2mat(struct2cell(regionprops(CC,'ConvexArea')));
sol = cell2mat(struct2cell(regionprops(CC,'Solidity')));
per = cell2mat(struct2cell(regionprops(CC,'Perimeter')));
area = cell2mat(struct2cell(regionprops(CC,'Area')));

convexity = area.^2 ./ conv.^2;
areadivper = area ./ per;

O = [ext;ecc;sol;areadivper;convexity]';
% 6 times kqrhbp and 6 times pbhrqk (reversed) plus hpphhpph to
% differenciate the horse and the pawn.
C = ['K','Q','R','H','B','P','K','Q','R','H','B','P','K','Q','R','H','B','P','K','Q','R','H','B','P','K','Q','R','H','B','P','K','Q','R','H','B','P','P','B','H','R','Q','K','P','B','H','R','Q','K','P','B','H','R','Q','K','P','B','H','R','Q','K','P','B','H','R','Q','K','P','B','H','R','Q','K','H','P','P','H','H','P','P','H']';
pred = TreeBagger(100,O,C);
% Fase II - Predict 
clc
%
I = rgb2gray(imread('Chess figures.png')); % original figures
BW = I < 128;
BW = medfilt2(BW,[5,5]);
BW = medfilt2(BW,[5,5]);
BW = bwareaopen(BW,40);
CC = bwconncomp(BW);

ext = cell2mat(struct2cell(regionprops(CC,'Extent')));
ecc = cell2mat(struct2cell(regionprops(CC,'Eccentricity')));
conv = cell2mat(struct2cell(regionprops(CC,'ConvexArea')));
sol = cell2mat(struct2cell(regionprops(CC,'Solidity')));
per = cell2mat(struct2cell(regionprops(CC,'Perimeter')));
area = cell2mat(struct2cell(regionprops(CC,'Area')));

convexity = area.^2 ./ conv.^2;
areadivper = area ./ per;

O = [ext;ecc;sol;areadivper;convexity]';

[ClassPredicted,Scores] = predict(pred,O);

Punts = sort(Scores');

Classes = {'B';'H';'K';'P';'Q';'R'}';
ClassPredicted';
vPuntuacio = (Punts(6,:)-Punts(5,:));
Puntuacio = sum(vPuntuacio)

S = num2cell(Scores);
K = cat(2,ClassPredicted,S);
K = cat(1,{'/';'B';'H';'K';'P';'Q';'R'}',K)
