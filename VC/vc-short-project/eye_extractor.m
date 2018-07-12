%% Gaze detector
% extracció de característiques
% m = sum(sum(I)) / 64*64
% d = distancia entre dos ulls, 0.65*d = area del ull
% 1 script per aprendre i per testejar
% ratio ulls no ulls = 1:10
path = 'C:\Users\wextia\Desktop\ShortProject\experiments\';
source_images_path = strcat(path, 'source-images\Short Project\');

dir_images_people = dir(strcat(source_images_path, '*.pgm'));
dir_eye_info = dir(strcat(source_images_path, '*.eye'));

n = length(dir_images_people);
index_images_eyes = 1;

look_path = strcat(path, 'images_look\');
no_look_path = strcat(path, 'images_no_look\');

dir_images_look = dir(strcat(look_path, '*.pgm'));
dir_images_no_look = dir(strcat(no_look_path, '*.pgm'));

index_dir_look = 1;
index_dir_no_look = 1;
index_images_look = 1;
index_images_no_look = 1;

images_look = zeros([length(dir_images_look),100,100]);
images_no_look = zeros([length(dir_images_no_look),100,100]);

images_eyes = zeros([2*n,100,100]);

for i = 1 : n
    image_person_name = dir_images_people(i).name;
    image_person = imread(strcat(source_images_path, image_person_name));
    
    s = size(image_person);
    l = length(s);
    if l == 3
        image_person = rgb2gray(image_person);
    end
    
    info_eye_name = dir_eye_info(i).name; 
    info_eye = importdata(strcat(source_images_path, info_eye_name),'');
    info_eye = cell2mat(cellfun(@str2num, info_eye(2), 'UniformOutput', 0));
        
    center_eye_1 = info_eye(1:2);
    center_eye_2 = info_eye(3:4);
    
    distance_eyes = pdist([center_eye_1; center_eye_2],'euclidean') * 0.65;
    
    image_eye_1 = getEyes(image_person, center_eye_1, distance_eyes);
    image_eye_1 = imresize(image_eye_1,[100 100]);
    
    image_eye_2 = getEyes(image_person, center_eye_2, distance_eyes);
    image_eye_2 = imresize(image_eye_2,[100 100]);
    
    images_eyes(index_images_eyes,:,:) = image_eye_1;
    images_eyes(index_images_eyes + 1,:,:) = image_eye_2;
    index_images_eyes = index_images_eyes + 2;
    
    if image_person_name == dir_images_look(index_dir_look).name
        images_look(index_images_look,:,:) = image_eye_1;
        images_look(index_images_look + 1,:,:) = image_eye_2;
        
        index_images_look = index_images_look + 2;
        index_dir_look = index_dir_look + 1;
    else
        images_no_look(index_images_no_look,:,:) = image_eye_1;
        images_no_look(index_images_no_look + 1,:,:) = image_eye_2;
        
        index_images_no_look = index_images_no_look + 2;
        index_dir_no_look = index_dir_no_look + 1;
    end
end

%% Store look and no look images

save images_look images_look
save images_no_look images_no_look
%% store images
n = length(images_eyes);
for index = 1 : n
    I = uint8(squeeze(images_eyes(index,:,:)));
    imwrite(I, strcat('eyes_', int2str(index), '.pgm'));
    I2 = flip(I ,2);
    imwrite(I2, strcat('eyes_', int2str(index), '_f.pgm'));
end
%% mostrem les imatges
for index = 1 : n
    I =  uint8(squeeze(images_eyes(index,:,:))); % squeeze elimina les dimensions que tenen mida 1 (singletons)
    imshow(I,[]);
end