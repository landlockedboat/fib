#version 330 core

layout (location = 0) in vec3 vertex;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec3 color;
layout (location = 3) in vec2 texCoord;

out vec4 frontColor;
out vec2 vtexCoord;

uniform mat4 modelViewMatrixInverse;
uniform mat4 modelViewProjectionMatrix;
uniform mat3 normalMatrix;

uniform vec4 lightPosition;

uniform float n=4;

void main()
{
    vec3 N = normalize(normalMatrix * normal);
    frontColor = vec4(1.0) * N.z;
    vtexCoord = texCoord;
	vec4 lightPosObs = modelViewMatrixInverse * lightPosition;
	float d = distance(vertex, lightPosObs.xyz);
	float w = clamp(1/pow(d,n), 0, 1); 
	vec3 magnetizedPos = (1.0-w)*vertex+w*lightPosObs.xyz;
    gl_Position = modelViewProjectionMatrix * vec4(magnetizedPos, 1.0);
}

float distance(vec3 p1, vec3 p2){
	float x = p1.x - p2.x;
	float y = p1.y - p2.y;
	float z = p1.z - p2.z;
	return sqrt(x*x + y*y + z*z);
}
