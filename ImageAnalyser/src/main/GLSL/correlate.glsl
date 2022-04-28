#version 120

precision mediump float;

uniform sampler2D u_image;

varying vec2 v_texCoord;
uniform vec2 u_resolution;
uniform float u_kernel[9];
uniform float u_kernel_sum;

void main(void) {
    vec2 onePixel = vec2(1.0, 1.0) / u_resolution;
    vec4 color = texture2D(u_image, (v_texCoord + onePixel) * vec2(-1, -1)) * u_kernel[0] +
    texture2D(u_image, (v_texCoord + onePixel) * vec2(0, -1)) * u_kernel[1] +
    texture2D(u_image, (v_texCoord + onePixel) * vec2(1, -1)) * u_kernel[2] +
    texture2D(u_image, (v_texCoord + onePixel) * vec2(-1, 0)) * u_kernel[3] +
    texture2D(u_image, (v_texCoord + onePixel) * vec2(0, 0)) * u_kernel[4] +
    texture2D(u_image, (v_texCoord + onePixel) * vec2(1, 0)) * u_kernel[5] +
    texture2D(u_image, (v_texCoord + onePixel) * vec2(-1, 1)) * u_kernel[6] +
    texture2D(u_image, (v_texCoord + onePixel) * vec2(0, 1)) * u_kernel[7] +
    texture2D(u_image, (v_texCoord + onePixel) * vec2(1, 1)) * u_kernel[8];
    gl_FragColor = vec4(abs(color / u_kernel_sum).rgb, 1.0);
}
