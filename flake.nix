{
    description = "Skills Telemetry API";

    inputs = {
        nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
        flake-utils.url = "github:numtide/flake-utils";
        sbt-deriv.url = "github:zaninime/sbt-derivation";
    };

    outputs = { self, nixpkgs, flake-utils, sbt-deriv, ... }: flake-utils.lib.eachDefaultSystem( system:
        let
            javaOverlay = final: prev: {
                jdk = prev.temurin-bin-25;
                jre = prev.temurin-jre-bin-25;
            };

            pkgs = import nixpkgs {
                inherit system;
                overlays = [ javaOverlay ];
            };
        in {
            devShells = {
                default = pkgs.mkShell {
                    buildInputs = [
                        pkgs.jdk
                        pkgs.sbt
                        pkgs.sqlite
                    ];
                    shellHook = ''
                        export JAVA_HOME=${pkgs.jdk}
                    '';
                };
            };

            packages = rec {
                default = app;

                app = sbt-deriv.lib.mkSbtDerivation {
                    inherit pkgs;

                    pname = "skills-telem";
                    version = "0.1.0";
                    src = ./.;

                    nativeBuildInputs = [ pkgs.jdk ];

                    depsSha256 = "sha256-GyoNAoZBkgcGYf8fmU0gQkP3lneUyA8UDoMeLZbCN4w=";

                    buildPhase = ''
                        sbt stage
                    '';

                    installPhase = ''
                        mkdir -p $out
                        cp -r target/universal/stage/* $out/
                    '';
                };
                
                container = pkgs.dockerTools.buildLayeredImage {
                    name = "skills-telem";
                    tag = "latest";
                    contents = [
                        app
                        pkgs.jre
                        pkgs.coreutils
                        pkgs.dockerTools.fakeNss
                        # pkgs.bash
                        # pkgs.dockerTools.usrBinEnv
                        # pkgs.dockerTools.binSh
                    ];
                    fakeRootCommands = ''
                        mkdir -p tmp data
                        chmod 1777 tmp
                        chown 1000:1000 data
                    '';
                    config = {
                        Cmd = [ "${app}/bin/skills-telem" ];
                        Env = [
                            # "JAVA_HOME=${pkgs.jre}"
                        ];
                        ExposedPorts = {
                            "9090/tcp" = {};
                        };
                        User = "1000:1000";
                    };
                };
            };
        }
    );
}