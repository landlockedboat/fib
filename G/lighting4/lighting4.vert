#version 330 core

layout (location = 0) in vec3 vertex;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec3 color;
layout (location = 3) in vec2 texCoord;

out vec4 frontColor;
out vec2 vtexCoord;

uniform mat4 modelViewMatrix;
uniform mat4 modelViewProjectionMatrix;
uniform mat3 normalMatrix;
uniform bool world;

uniform vec4 lightPosition;

out vec3 P, N, V, L;


void main()
{
	if(!world){
		N = normalize(normalMatrix * normal);
	}
	else{
		N = normalize(normalMatrix * normal);
	}
	P = (modelViewMatrix * vec4(vertex.xyz, 1.0)).xyz;
	V = -P;
	L = (lightPosition.xyz - P);
	gl_Position = modelViewProjectionMatrix * vec4(vertex.xyz, 1.0);
}
